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
import org.jbpm.formbuilder.client.form.FBInplaceEditor;
import org.jbpm.formbuilder.client.form.editors.HeaderInplaceEditor;
import org.jbpm.formbuilder.shared.rep.FormItemRepresentation;
import org.jbpm.formbuilder.shared.rep.items.HeaderRepresentation;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

/**
 * UI form item. Represents a header or title
 */
public class HeaderFormItem extends FBFormItem {


    private final HTML header = new HTML("<h1>Header</h1>");
    
    private String id;
    private String name;
    private String cssClassName;

    public HeaderFormItem() {
        this(new ArrayList<FBFormEffect>());
    }
    
    public HeaderFormItem(List<FBFormEffect> formEffects) {
        super(formEffects);
        add(getHeader());
        setWidth("100%");
        setHeight("30px");
        getHeader().setSize(getWidth(), getHeight());
    }
    
    @Override
    public Map<String, Object> getFormItemPropertiesMap() {
        Map<String, Object> formItemPropertiesMap = new HashMap<String, Object>();
        formItemPropertiesMap.put("id", id);
        formItemPropertiesMap.put("name", name);
        formItemPropertiesMap.put("width", getWidth());
        formItemPropertiesMap.put("height", getHeight());
        formItemPropertiesMap.put("cssClassName", cssClassName);
        return formItemPropertiesMap;
    }

    @Override
    public FBInplaceEditor createInplaceEditor() {
        return new HeaderInplaceEditor(this);
    }

    @Override
    public void saveValues(Map<String, Object> propertiesMap) {
        this.id = extractString(propertiesMap.get("id"));
        this.name = extractString(propertiesMap.get("name"));
        setWidth(extractString(propertiesMap.get("width")));
        setHeight(extractString(propertiesMap.get("height")));
        this.cssClassName = extractString(propertiesMap.get("cssClassName"));
        populate(getHeader());
    }

    private void populate(HTML html) {
        if (getWidth() != null) {
            html.setWidth(getWidth());
        }
        if (this.getHeight() != null) {
            html.setHeight(getHeight());
        }
        if (this.cssClassName != null) {
            html.setStyleName(this.cssClassName);
        }
    }
    
    public HTML getHeader() {
        return this.header;
    }
    
    public void setContent(String html) {
        getHeader().setHTML(html);
    }
    
    @Override
    public void addEffect(FBFormEffect effect) {
        super.addEffect(effect);
        effect.setWidget(this.header);
    }
    
    @Override
    public FormItemRepresentation getRepresentation() {
        HeaderRepresentation rep = super.getRepresentation(new HeaderRepresentation());
        rep.setValue(this.header.getText());
        rep.setStyleClass(this.cssClassName);
        rep.setCssId(this.id);
        rep.setCssName(this.name);
        return rep;
    }
    
    @Override
    public void populate(FormItemRepresentation rep) throws FormBuilderException {
        if (!(rep instanceof HeaderRepresentation)) {
            throw new FormBuilderException("rep should be of type LabelRepresentation but is of type " + rep.getClass().getName());
        }
        super.populate(rep);
        HeaderRepresentation hrep = (HeaderRepresentation) rep;
        this.cssClassName = hrep.getCssName();
        this.id = hrep.getCssId();
        if (hrep.getValue().startsWith("<h1>")) {
            setContent(hrep.getValue());
        } else {
            setContent("<h1>" + hrep.getValue() + "</h1>");
        }
    }
    
    @Override
    public FBFormItem cloneItem() {
        HeaderFormItem clone = super.cloneItem(new HeaderFormItem(getFormEffects()));
        clone.cssClassName = this.cssClassName;
        clone.id = this.id;
        clone.name = this.name;
        clone.setContent(this.header.getHTML());
        clone.populate(this.header);
        return clone;
    }
    
    @Override
    public Widget cloneDisplay() {
        HTML html = new HTML(this.header.getHTML());
        populate(html);
        return html;
    }
}
