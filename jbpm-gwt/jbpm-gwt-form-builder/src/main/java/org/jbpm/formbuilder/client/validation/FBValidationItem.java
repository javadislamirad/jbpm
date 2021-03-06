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

import java.util.HashMap;
import java.util.Map;

import org.jbpm.formbuilder.client.FormBuilderException;
import org.jbpm.formbuilder.client.bus.ui.NotificationEvent;
import org.jbpm.formbuilder.client.bus.ui.NotificationEvent.Level;
import org.jbpm.formbuilder.client.resources.FormBuilderGlobals;
import org.jbpm.formbuilder.shared.form.FormEncodingException;
import org.jbpm.formbuilder.shared.rep.FBValidation;
import org.jbpm.formbuilder.shared.rep.RepresentationFactory;

import com.google.gwt.user.client.rpc.impl.ReflectionHelper;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;

/**
 * Base UI component for a validation class
 * 
 * TODO use this
 */
public abstract class FBValidationItem {

    private final Map<String, HasValue<String>> propertiesMap = new HashMap<String, HasValue<String>>();
    
    public FBValidationItem() {
    }
    
    public Map<String, HasValue<String>> getPropertiesMap() {
        return propertiesMap;
    }
    
    public void populatePropertiesMap(Map<String, HasValue<String>> map) {
        propertiesMap.putAll(map);
    }
    
    public <T extends FBValidation> T getRepresentation(T representation) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        for (Map.Entry<String, HasValue<String>> entry : propertiesMap.entrySet()) {
            dataMap.put(entry.getKey(), entry.getValue().getValue());
        }
        try {
            representation.setDataMap(dataMap);
        } catch (FormEncodingException e) {
            FormBuilderGlobals.getInstance().getEventBus().fireEvent(
                    new NotificationEvent(Level.ERROR, "Couldn't create validation item representation", e));
        }
        return representation;
    }
    
    public abstract String getName();

    public abstract FBValidation createValidation();
    
    public abstract Widget createDisplay();

    public abstract FBValidationItem cloneItem();
    
    public abstract void populate(FBValidation validation) throws FormBuilderException;

    public static FBValidationItem createValidation(FBValidation validationRep) throws FormBuilderException {
        try {
            String repClassName = (String) validationRep.getDataMap().get("@className");
            String className = RepresentationFactory.getItemClassName(repClassName);
            Class<?> clazz = ReflectionHelper.loadClass(className);
            Object obj = ReflectionHelper.newInstance(clazz);
            FBValidationItem item = (FBValidationItem) obj;
            item.populate(validationRep);
            return item;
        } catch (Exception e) {
            throw new FormBuilderException(e);
        }
    }
}
