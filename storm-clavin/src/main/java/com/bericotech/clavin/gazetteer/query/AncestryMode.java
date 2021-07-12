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
 * HierarchyMode.java
 *
 *###################################################################*/

package com.bericotech.clavin.gazetteer.query;

/**
 * Controls the resolution of the hierarchy of political divisions
 * containing a ResolvedLocation.
 */
public enum AncestryMode {
    /**
     * Resolve the hierarchy when locations are created.
     */
    ON_CREATE,
    /**
     * Resolve the hierarchy when the parent of a location is
     * requested. Lazily loaded hierarchies may also be manually
     * loaded to improve performance by loading the hierarchy
     * for multiple locations at once.
     */
    LAZY,
    /**
     * Do not resolve hierarchy unless manually requested.
     */
    MANUAL;
}
