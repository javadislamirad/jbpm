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
package org.jbpm.formbuilder.client.validation;

import java.util.Map;

import org.jbpm.formbuilder.client.FormBuilderException;
import org.jbpm.formbuilder.shared.rep.FBValidation;
import org.jbpm.formbuilder.shared.rep.validation.NotEmptyValidation;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class NotEmptyValidationItem extends FBValidationItem {

    @Override
    public String getName() {
        return "Not Empty";
    }

    @Override
    public Map<String, HasValue<String>> getPropertiesMap() {
        Map<String, HasValue<String>> map = super.getPropertiesMap();
        if (!map.containsKey("errorMessage")) {
            map.put("errorMessage", new TextBox());
        }
        return map;
    }
    
    @Override
    public void populate(FBValidation validation) throws FormBuilderException {
        if (!(validation instanceof NotEmptyValidation)) {
            throw new FormBuilderException("validation should be of type NotEmptyValidation but is of type " + validation.getClass().getName());
        }
    }
    
    @Override
    public FBValidation createValidation() {
        return getRepresentation(new NotEmptyValidation());
    }

    @Override
    public Widget createDisplay() {
        return null;
    }

    @Override
    public FBValidationItem cloneItem() {
        NotEmptyValidationItem item = new NotEmptyValidationItem();
        item.populatePropertiesMap(getPropertiesMap());
        return item;
    }

}
