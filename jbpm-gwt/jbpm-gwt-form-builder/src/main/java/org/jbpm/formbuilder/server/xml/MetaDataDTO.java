/**
 * Copyright 2011 JBoss Inc 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jbpm.formbuilder.server.xml;

import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;

public class MetaDataDTO {

    private String _key;
    private String _value;
    
    public MetaDataDTO() {
        // jaxb needs a default constructor
    }
    
    public MetaDataDTO(Map.Entry<String, String> entry) {
        this._key = entry.getKey();
        this._value = entry.getValue();
    }

    @XmlAttribute 
    public String getKey() {
        return _key;
    }

    public void setKey(String key) {
        this._key = key;
    }

    @XmlAttribute 
    public String getValue() {
        return _value;
    }

    public void setValue(String value) {
        this._value = value;
    }
}
