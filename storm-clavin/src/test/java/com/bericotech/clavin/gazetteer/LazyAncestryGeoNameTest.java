/*#####################################################################
 *
 * CLAVIN (Cartographic Location And Vicinity INdexer)
 * ---------------------------------------------------
 *
 * Copyright (C) 2012-2013 Berico Technologies
 * http://clavin.bericotechnologies.com
 *
 * ====================================================================
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * ====================================================================
 *
 * LazyAncestryGeoNameTest.java
 *
 *###################################################################*/

package com.bericotech.clavin.gazetteer;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import com.bericotech.clavin.gazetteer.query.AncestryMode;
import com.bericotech.clavin.gazetteer.query.Gazetteer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Tests to ensure ancestry is lazily resolved.
 */
@RunWith(MockitoJUnitRunner.class)
public class LazyAncestryGeoNameTest {
    private static final int TEST_PARENT_ID = 42;

    @Mock
    private Gazetteer gazetteer;
    @Mock
    private GeoName geoName;
    @Mock
    private GeoName parent;

    @Test
    public void testGetParent_ManualResolve_NullParent() {
        LazyAncestryGeoName instance = new LazyAncestryGeoName(geoName, TEST_PARENT_ID);
        when(geoName.getParent()).thenReturn(null);
        assertNull("Expected null parent for GeoName.", instance.getParent());
        verify(geoName, never()).setParent(any(GeoName.class));
    }

    @Test
    public void testGetParent_ManualResolve_WithParent() {
        LazyAncestryGeoName instance = new LazyAncestryGeoName(geoName, TEST_PARENT_ID);
        when(geoName.getParent()).thenReturn(parent);
        assertEquals("Expected configured parent", parent, instance.getParent());
        verify(geoName, never()).setParent(any(GeoName.class));
    }

    @Test
    public void testGetParent_LazyResolve_NotResolved_NoParent() throws Exception {
        LazyAncestryGeoName instance = new LazyAncestryGeoName(geoName, TEST_PARENT_ID, gazetteer);
        when(geoName.isAncestryResolved()).thenReturn(false);
        when(gazetteer.getGeoName(TEST_PARENT_ID, AncestryMode.ON_CREATE)).thenReturn(null);
        assertNull("Expecting no parent resolved.", instance.getParent());
        verify(geoName).setParent(null);
    }

    @Test
    public void testGetParent_LazyResolve_NotResolved_FoundParent() throws Exception {
        LazyAncestryGeoName instance = new LazyAncestryGeoName(geoName, TEST_PARENT_ID, gazetteer);
        when(geoName.isAncestryResolved()).thenReturn(false);
        when(geoName.getParent()).thenReturn(parent);
        when(gazetteer.getGeoName(TEST_PARENT_ID, AncestryMode.ON_CREATE)).thenReturn(parent);
        assertEquals("Expected resolution to mock parent.", parent, instance.getParent());
        verify(geoName).setParent(parent);
    }

    @Test
    public void testGetParent_LazyResolve_AlreadyResolved() throws Exception {
        LazyAncestryGeoName instance = new LazyAncestryGeoName(geoName, TEST_PARENT_ID, gazetteer);
        when(geoName.isAncestryResolved()).thenReturn(true);
        when(geoName.getParent()).thenReturn(parent);
        assertEquals("Expected resolved parent", parent, instance.getParent());
        verify(geoName, never()).setParent(any(GeoName.class));
        verify(gazetteer, never()).getGeoName(anyInt(), any(AncestryMode.class));
    }

    @Test
    public void testGetParent_LazyResolve_NullParentId() throws Exception {
        LazyAncestryGeoName instance = new LazyAncestryGeoName(geoName, null, gazetteer);
        when(geoName.getParent()).thenReturn(null);
        when(geoName.isAncestryResolved()).thenReturn(false);
        assertNull("Expected null parent", instance.getParent());
        verify(geoName, never()).setParent(any(GeoName.class));
        verify(gazetteer, never()).getGeoName(anyInt(), any(AncestryMode.class));
    }
}
