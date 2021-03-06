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

import org.jbpm.formbuilder.client.FormBuilderException;
import org.jbpm.formbuilder.client.FormBuilderService;
import org.jbpm.formbuilder.client.bus.GetFormRepresentationEvent;
import org.jbpm.formbuilder.client.bus.ui.NotificationEvent;
import org.jbpm.formbuilder.client.bus.ui.NotificationEvent.Level;
import org.jbpm.formbuilder.client.bus.GetFormRepresentationResponseEvent;
import org.jbpm.formbuilder.client.bus.GetFormRepresentationResponseHandler;
import org.jbpm.formbuilder.client.bus.UndoableEvent;
import org.jbpm.formbuilder.client.bus.UndoableHandler;
import org.jbpm.formbuilder.client.resources.FormBuilderGlobals;
import org.jbpm.formbuilder.shared.rep.FormRepresentation;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Handles the action of deleting a form on the server.
 */
public class DeleteFormCommand implements BaseCommand {

    private static final String DELETE_TYPE = DeleteFormCommand.class.getName();
    
    private final EventBus bus = FormBuilderGlobals.getInstance().getEventBus();
    private final FormBuilderService service = FormBuilderGlobals.getInstance().getService();
    
    public DeleteFormCommand() {
        super();
        bus.addHandler(GetFormRepresentationResponseEvent.TYPE, new GetFormRepresentationResponseHandler() {
            public void onEvent(GetFormRepresentationResponseEvent event) {
                if (DELETE_TYPE.equals(event.getSaveType())) {
                    FormRepresentation form = event.getRepresentation();
                    showDeletePanel(form);
                }
            }
        });
    }

    public void execute() {
        bus.fireEvent(new GetFormRepresentationEvent(DELETE_TYPE));
    }

    public void setItem(MenuItem item) {
        item.setEnabled(true);
    }

    private void showDeletePanel(final FormRepresentation form) {
        final PopupPanel panel = new PopupPanel();
        if (form.isSaved()) {
            VerticalPanel vpanel = new VerticalPanel();
            vpanel.add(new Label("Warning! You will delete form " + form.getName() + " from the server. " +
                    "Are you sure you want to continue?"));
            HorizontalPanel hpanel = new HorizontalPanel();
            Button confirmButton = new Button("Confirm");
            confirmButton.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    deleteForm(form);
                    panel.hide();
                }
            });
            hpanel.add(confirmButton);
            Button cancelButton = new Button("Cancel");
            cancelButton.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    panel.hide();
                }
            });
            hpanel.add(cancelButton);
            vpanel.add(hpanel);
            panel.setWidget(vpanel);
        } else {
            VerticalPanel vpanel = new VerticalPanel();
            vpanel.add(new Label("Form was never saved"));
            Button closeButton = new Button("Close");
            closeButton.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    panel.hide();
                }
            });
            vpanel.add(closeButton);
        }
        int height = RootPanel.getBodyElement().getClientHeight();
        int width = RootPanel.getBodyElement().getClientWidth();
        panel.setPopupPosition((width / 2) - 150, (height - 100) / 2);
        panel.show();
    }
    
    private void deleteForm(FormRepresentation form) {
        Map<String, Object> dataSnapshot = new HashMap<String, Object>();
        dataSnapshot.put("form", form);
        bus.fireEvent(new UndoableEvent(dataSnapshot, new UndoableHandler() {
            public void undoAction(UndoableEvent event) {
                FormRepresentation form = (FormRepresentation) event.getData("form");
                if (form != null) {
                    try {
                        service.saveForm(form);
                    } catch (FormBuilderException e) {
                        bus.fireEvent(new NotificationEvent(Level.ERROR, "Problem restoring form", e));
                    }
                }
            }
            public void onEvent(UndoableEvent event) { }
            public void doAction(UndoableEvent event) {
                FormRepresentation form = (FormRepresentation) event.getData("form");
                try {
                    service.deleteForm(form);
                } catch (FormBuilderException e) {
                    event.setData("form", null);
                    bus.fireEvent(new NotificationEvent(Level.ERROR, "Problem deleting form", e));
                }
            }
        }));
    }
}
