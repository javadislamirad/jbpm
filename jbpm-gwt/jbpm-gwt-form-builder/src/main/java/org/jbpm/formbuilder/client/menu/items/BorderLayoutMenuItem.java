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
package org.jbpm.formbuilder.client.menu.items;

import java.util.List;

import org.jbpm.formbuilder.client.effect.FBFormEffect;
import org.jbpm.formbuilder.client.form.FBFormItem;
import org.jbpm.formbuilder.client.form.items.BorderLayoutFormItem;
import org.jbpm.formbuilder.client.menu.FBMenuItem;
import org.jbpm.formbuilder.client.resources.FormBuilderResources;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Label;

public class BorderLayoutMenuItem extends FBMenuItem {

	public BorderLayoutMenuItem() {
		super();
	}
	
    public BorderLayoutMenuItem(List<FBFormEffect> formEffects) {
		super(formEffects);
	}

	@Override
    protected ImageResource getIconUrl() {
        return FormBuilderResources.INSTANCE.borderLayoutIcon();
    }

    @Override
    public Label getDescription() {
        return new Label("Border Layout");
    }

    @Override
    public FBMenuItem cloneWidget() {
        return new BorderLayoutMenuItem(getFormEffects());
    }

    @Override
    public FBFormItem buildWidget() {
        return new BorderLayoutFormItem(getFormEffects());
    }

}
