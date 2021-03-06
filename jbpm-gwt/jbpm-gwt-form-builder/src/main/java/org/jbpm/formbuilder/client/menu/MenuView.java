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

import java.util.HashMap;
import java.util.Map;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * menu view. Displays the menu items accordion
 */
public class MenuView extends ScrollPanel {

    private Map<String, FBMenuGroupPanel> accordion = new HashMap<String, FBMenuGroupPanel>();
    
    private VerticalPanel panel = new VerticalPanel();
    private PickupDragController dragController;
    
    public MenuView() {
        setSize("100%", "100%");
        panel.setSize("100%", "100%");
        panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
        add(panel);
    }
    
    public void setDragController(PickupDragController dragController) {
        this.dragController = dragController;
    }
    
    public void addItem(String group, FBMenuItem item) {
        if (accordion.get(group) == null) {
            FBMenuPanel menuPanel = new FBMenuPanel(this.dragController);
            FBMenuGroupPanel groupPanel = new FBMenuGroupPanel(group, menuPanel);
            accordion.put(group, groupPanel);
            panel.add(groupPanel);
        }
        accordion.get(group).add(item);
    }

    public void removeItem(String group, FBMenuItem item) {
        if (accordion.get(group) != null) {
            FBMenuGroupPanel groupPanel = accordion.get(group);
            groupPanel.remove(item);
            if (!groupPanel.hasWidgets()) {
                accordion.remove(groupPanel);
                panel.remove(groupPanel);
            }
        }
    }
}
