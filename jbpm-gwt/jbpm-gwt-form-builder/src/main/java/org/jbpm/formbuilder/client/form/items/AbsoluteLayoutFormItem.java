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
import org.jbpm.formbuilder.shared.rep.items.AbsolutePanelRepresentation;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

/**
 * UI form layout item. Represents an absolute layout
 */
public class AbsoluteLayoutFormItem extends LayoutFormItem {

    private AbsolutePanel panel = new AbsolutePanel() {
        @Override
        public boolean remove(Widget widget) {
            if (widget instanceof FBFormItem) {
                AbsoluteLayoutFormItem.this.remove(widget);
            }
            return super.remove(widget);
        }
    };
    
    private String id;

    public AbsoluteLayoutFormItem() {
        this(new ArrayList<FBFormEffect>());
    }
    
    public AbsoluteLayoutFormItem(List<FBFormEffect> formEffects) {
        super(formEffects);
        add(panel);
        setSize("90px", "90px");
        panel.setSize(getWidth(), getHeight());
    }

    @Override
    public Map<String, Object> getFormItemPropertiesMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", this.id);
        map.put("height", getHeight());
        map.put("width", getWidth());
        return map;
    }
    
    @Override
    public void saveValues(Map<String, Object> asPropertiesMap) {
        this.id = extractString(asPropertiesMap.get("id"));
        this.setHeight(extractString(asPropertiesMap.get("height")));
        this.setWidth(extractString(asPropertiesMap.get("width")));
        populate(this.panel);
    }

    private void populate(AbsolutePanel panel) {
        if (this.getHeight() != null) {
            panel.setHeight(this.getHeight());
        } 
        if (this.getWidth() != null) {
            panel.setWidth(this.getWidth());
        }
    }

    @Override
    public FormItemRepresentation getRepresentation() {
        AbsolutePanelRepresentation rep = super.getRepresentation(new AbsolutePanelRepresentation());
        rep.setId(this.id);
        for (FBFormItem item : getItems()) {
            rep.addItem(item.getRepresentation(), 
                    item.getDesiredX() - panel.getAbsoluteLeft(), 
                    item.getDesiredY() - panel.getAbsoluteTop());
        }
        return rep;
    }
    
    @Override
    public void populate(FormItemRepresentation rep) throws FormBuilderException {
        if (!(rep instanceof AbsolutePanelRepresentation)) {
            throw new FormBuilderException("rep should be of type AbsolutePanelRepresentation but is of type " + rep.getClass().getName());
        }
        super.populate(rep);
        AbsolutePanelRepresentation arep = (AbsolutePanelRepresentation) rep;
        panel.clear();
        getItems().clear();
        if (arep.getItems() != null) {
            for (Map.Entry<AbsolutePanelRepresentation.Position, FormItemRepresentation> entry : arep.getItems().entrySet()) {
                FBFormItem item = super.createItem(entry.getValue());
                item.setDesiredPosition(entry.getKey().getX(), entry.getKey().getY());
                this.add(item);
            }
        }
        populate(this.panel);
    }

    @Override
    public FBFormItem cloneItem() {
        AbsoluteLayoutFormItem clone = new AbsoluteLayoutFormItem(getFormEffects());
        clone.setHeight(this.getHeight());
        clone.id = this.id;
        clone.setWidth(this.getWidth());
        clone.populate(clone.panel);
        for (FBFormItem item : getItems()) {
            clone.add(item.cloneItem());
        }
        return clone;
    }

    @Override
    public boolean add(FBFormItem item) {
        int left = item.getDesiredX();
        int top = item.getDesiredY();
        panel.add(item, left - panel.getAbsoluteLeft(), top - panel.getAbsoluteTop());
        return super.add(item);
    }
    
    @Override
    public boolean remove(Widget child) {
        return super.remove(child);
    }
    
    @Override
    public Panel getPanel() {
        return this.panel;
    }

    @Override
    public Widget cloneDisplay() {
        AbsolutePanel ap = new AbsolutePanel();
        populate(ap);
        for (FBFormItem item : getItems()) {
            ap.add(item.cloneDisplay(), this.getAbsoluteLeft() - item.getDesiredX(), this.getAbsoluteTop() - item.getDesiredY());
        }
        return ap;
    }
}
