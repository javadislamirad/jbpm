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
package org.jbpm.formbuilder.client.form.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jbpm.formbuilder.client.FormBuilderException;
import org.jbpm.formbuilder.client.effect.FBFormEffect;
import org.jbpm.formbuilder.client.form.FBFormItem;
import org.jbpm.formbuilder.shared.rep.FormItemRepresentation;
import org.jbpm.formbuilder.shared.rep.items.HiddenRepresentation;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.Widget;

/**
 * UI form item. Represents a hidden field
 */
public class HiddenFormItem extends FBFormItem {

    private Hidden hidden = new Hidden();
    
    private String id;
    private String name;
    private String value;

    public HiddenFormItem() {
        this(new ArrayList<FBFormEffect>());
    }
    
    public HiddenFormItem(List<FBFormEffect> formEffects) {
        super(formEffects);
        Grid border = new Grid(1, 1);
        border.setSize("100px", "20px");
        border.setBorderWidth(1);
        border.setWidget(0, 0, hidden);
        add(border);
        setSize("100px", "20px");
    }

    @Override
    public Map<String, Object> getFormItemPropertiesMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("value", this.value);
        map.put("name", this.name);
        map.put("id", this.id);
        return map;
    }
    
    @Override
    public void saveValues(Map<String, Object> asPropertiesMap) {
        this.value = asPropertiesMap.get("value").toString();
        this.name = asPropertiesMap.get("name").toString();
        this.id = asPropertiesMap.get("id").toString();
        populate(this.hidden);
    }

    private void populate(Hidden hidden) {
        if (this.id != null) {
            hidden.setID(id);
        }
        if (this.name != null) {
            hidden.setName(name);
        }
        if (this.value != null) {
            hidden.setValue(value);
        }
    }

    @Override
    public FormItemRepresentation getRepresentation() {
        HiddenRepresentation rep = super.getRepresentation(new HiddenRepresentation());
        rep.setId(id);
        rep.setName(name);
        rep.setValue(value);
        return rep;
    }
    
    @Override
    public void populate(FormItemRepresentation rep) throws FormBuilderException {
        if (!(rep instanceof HiddenRepresentation)) {
            throw new FormBuilderException("rep should be of type HiddenRepresentation but is of type " + rep.getClass().getName());
        }
        super.populate(rep);
        HiddenRepresentation hrep = (HiddenRepresentation) rep;
        this.id = hrep.getId();
        this.name = hrep.getName();
        this.value = hrep.getValue();
        populate(this.hidden);
    }

    @Override
    public FBFormItem cloneItem() {
        HiddenFormItem clone = new HiddenFormItem(getFormEffects());
        clone.id = this.id;
        clone.name = this.name;
        clone.value = this.value;
        clone.populate(clone.hidden);
        return clone;
    }
    
    @Override
    public Widget cloneDisplay() {
        Hidden hi = new Hidden();
        populate(hi);
        return hi;
    }
}
