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
package org.jbpm.formbuilder.client.command;

import java.util.HashMap;
import java.util.Map;

import org.jbpm.formbuilder.client.bus.UndoableEvent;
import org.jbpm.formbuilder.client.bus.UndoableHandler;
import org.jbpm.formbuilder.client.form.FBForm;
import org.jbpm.formbuilder.client.form.FBFormItem;
import org.jbpm.formbuilder.client.form.items.LayoutFormItem;
import org.jbpm.formbuilder.client.layout.LayoutView;
import org.jbpm.formbuilder.client.menu.FBMenuItem;
import org.jbpm.formbuilder.client.resources.FormBuilderGlobals;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.drop.AbstractDropController;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

/**
 * DropController designed to allow dropping of components inside
 * the form layout and fire all necessary actions.
 */
public class DropFormItemController extends AbstractDropController {

    private final LayoutView layoutView;
    private final EventBus bus;
    
    public DropFormItemController(Widget dropTarget, LayoutView layoutView) {
        super(dropTarget);
        this.layoutView = layoutView;
        this.bus = FormBuilderGlobals.getInstance().getEventBus();
    }
    
    @Override
    public void onDrop(DragContext context) {
        Widget drag = context.draggable;
        int x = context.mouseX;
        int y = context.mouseY;
        if (drag != null && drag instanceof FBMenuItem) { //when you add a component from the menu
            FBMenuItem menuItem = (FBMenuItem) drag;
            FBFormItem formItem = menuItem.buildWidget();
            formItem.setDesiredPosition(x, y);
            Map<String, Object> dataSnapshot = new HashMap<String, Object>();
            dataSnapshot.put("formItem", formItem);
            dataSnapshot.put("menuItem", menuItem);
            dataSnapshot.put("x", x);
            dataSnapshot.put("y", y);
            this.bus.fireEvent(new UndoableEvent(dataSnapshot, new UndoableHandler() {
                public void onEvent(UndoableEvent event) {  }
                public void undoAction(UndoableEvent event) {
                    FBFormItem formItem = (FBFormItem) event.getData("formItem");
                    Integer x = (Integer) event.getData("x");
                    Integer y = (Integer) event.getData("y");
                    Panel panel = layoutView.getUnderlyingLayout(x, y);
                    panel.remove(formItem);
                }
                public void doAction(UndoableEvent event) {
                    FBFormItem formItem = (FBFormItem) event.getData("formItem");
                    FBMenuItem menuItem = (FBMenuItem) event.getData("menuItem");
                    Integer x = (Integer) event.getData("x");
                    Integer y = (Integer) event.getData("y");
                    if (formItem != null) {
                        Panel panel = layoutView.getUnderlyingLayout(x, y);
                        if (panel instanceof FBForm) {
                            panel.remove(menuItem);
                            panel.add(formItem);
                        } else {
                            LayoutFormItem layoutItem = (LayoutFormItem) panel.getParent();
                            panel.remove(menuItem);
                            layoutItem.add(formItem);
                        }
                    }
                }
            }));
        }
    }
}
