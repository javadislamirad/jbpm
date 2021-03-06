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
import org.jbpm.formbuilder.client.bus.ui.GetFormDisplayEvent;
import org.jbpm.formbuilder.client.form.FBForm;
import org.jbpm.formbuilder.client.form.FBFormItem;
import org.jbpm.formbuilder.client.form.items.LayoutFormItem;
import org.jbpm.formbuilder.client.resources.FormBuilderGlobals;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.Widget;

/**
 * Handles the action of pasting a component
 */
public class PasteCommand extends AbstractCopyPasteCommand {

    private final EventBus bus = FormBuilderGlobals.getInstance().getEventBus();
    
    public PasteCommand() {
        super();
        FormBuilderGlobals.getInstance().register(this);
    }
    
    @Override
    protected void enable(MenuItem menuItem) {
        menuItem.setEnabled(AbstractCopyPasteCommand.getMemory() != null);
    }
    
    public void execute() {
        Map<String, Object> dataSnapshot = new HashMap<String, Object>();
        dataSnapshot.put("itemToHold", getSelectedItem() == null ? getFormDisplay() : getSelectedItem());
        dataSnapshot.put("memory", AbstractCopyPasteCommand.getMemory());
        fireUndoableEvent(dataSnapshot, new UndoableHandler() {
            public void doAction(UndoableEvent event) {
                Widget itemToHold = (Widget) event.getData("itemToHold");
                Object obj = event.getData("memory");
                if (obj instanceof FBFormItem) {
                    FBFormItem itemToPaste = (FBFormItem) obj;
                    itemToPaste = itemToPaste.cloneItem();
                    if (itemToHold == null) {
                        getFormDisplay().add(itemToPaste);
                    } else {
                        if (itemToHold instanceof LayoutFormItem) {
                            LayoutFormItem parentPanel = (LayoutFormItem) itemToHold;
                            parentPanel.add(itemToPaste);
                        } else if (itemToHold instanceof HasOneWidget) {
                            HasOneWidget parentPanel = (HasOneWidget) itemToHold;
                            parentPanel.setWidget(itemToPaste);
                        } else if (itemToHold instanceof HasWidgets) {
                            HasWidgets parentPanel = (HasWidgets) itemToHold;
                            parentPanel.add(itemToPaste);
                        } 
                    }
                }
            }
            public void onEvent(UndoableEvent event) { }
            public void undoAction(UndoableEvent event) {
                FBFormItem itemToHold = (FBFormItem) event.getData("itemToHold");
                Object obj = event.getData("memory");
                if (itemToHold != null && obj instanceof FBFormItem) {
                    FBFormItem itemToPaste = (FBFormItem) obj;
                    itemToPaste.removeFromParent();
                }
            }
        });
    }
    
    private FBForm getFormDisplay() {
        GetFormDisplayEvent event = new GetFormDisplayEvent();
        bus.fireEvent(event);
        return event.getFormDisplay();
    }
}
