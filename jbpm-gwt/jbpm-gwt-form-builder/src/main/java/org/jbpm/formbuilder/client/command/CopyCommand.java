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
import org.jbpm.formbuilder.client.form.FBFormItem;
import org.jbpm.formbuilder.client.resources.FormBuilderGlobals;

import com.google.gwt.user.client.ui.MenuItem;

/**
 * Handles the action of copying a ui component
 */
public class CopyCommand extends AbstractCopyPasteCommand {

    public CopyCommand() {
        super();
        FormBuilderGlobals.getInstance().register(this);
    }
    
    @Override
    protected void enable(MenuItem menuItem) {
        menuItem.setEnabled(getSelectedItem() != null);
    }
    
    public void execute() {
        Map<String, Object> dataSnapshot = new HashMap<String, Object>();
        dataSnapshot.put("selectedItem", getSelectedItem());
        dataSnapshot.put("oldMemory", AbstractCopyPasteCommand.getMemory());
        fireUndoableEvent(dataSnapshot, new UndoableHandler() {
            public void doAction(UndoableEvent event) {
                FBFormItem item = (FBFormItem) event.getData("selectedItem");
                if (item == null) {
                    AbstractCopyPasteCommand.setMemory(null);
                } else {
                    AbstractCopyPasteCommand.setMemory(item.cloneItem());
                }
                FormBuilderGlobals.getInstance().paste().enable();
            }
            public void undoAction(UndoableEvent event) {
                Object oldMemory = event.getData("oldMemory");
                AbstractCopyPasteCommand.setMemory(oldMemory);
                FormBuilderGlobals.getInstance().paste().enable();
            }
            public void onEvent(UndoableEvent event) { }
        });
    }
}
