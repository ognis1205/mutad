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
 * ClavinException.java
 *
 *###################################################################*/

package com.bericotech.clavin;

/**
 * A wrapper exception for errors occurring when resolving locations with CLAVIN.
 */
public class ClavinException extends Exception {
    /**
     * Create a new ClavinException.
     */
    public ClavinException() {
    }

    /**
     * Create a new ClavinException with the given message.
     * @param message the message
     */
    public ClavinException(final String message) {
        super(message);
    }

    /**
     * Create a new ClavinException with the given message and cause.
     * @param message the message
     * @param cause  the cause
     */
    public ClavinException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Create a new ClavinException with the given cause.
     * @param cause the cause
     */
    public ClavinException(Throwable cause) {
        super(cause);
    }
}
