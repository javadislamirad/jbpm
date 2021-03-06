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
package org.jbpm.formbuilder.client.menu;

import java.util.ArrayList;
import java.util.List;

import org.jbpm.formbuilder.client.effect.FBFormEffect;
import org.jbpm.formbuilder.client.form.FBFormItem;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * Effects popup. Shows all valid effects for a given
 * {@link FBFormItem}, once the user click the right
 * button over it.
 */
public class EffectsPopupPanel extends PopupPanel {

    private List<FBFormEffect> effects = new ArrayList<FBFormEffect>();
    
    public EffectsPopupPanel(final FBFormItem item, boolean autoHide) {
        super(autoHide);
        MenuBar bar = new MenuBar(true);
        this.effects = item.getFormEffects();
        for (final FBFormEffect effect : effects) {
            if (effect.isValidForItem(item)) {
                bar.addItem(new MenuItem(
                    effect.getName(), 
                    new Command() {
                        public void execute() {
                            PopupPanel popup = effect.createPanel();
                            if (popup != null) {
                                popup.setPopupPosition(getPopupLeft(), getPopupTop() + 30);
                                popup.show();
                            } else {
                                hide();
                            }
                            effect.apply(item);
                        }
                    })
                );
            }
        }
        add(bar);
    }
}
