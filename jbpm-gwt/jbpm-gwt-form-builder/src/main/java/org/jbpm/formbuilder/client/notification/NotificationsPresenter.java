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
package org.jbpm.formbuilder.client.notification;

import org.jbpm.formbuilder.client.bus.ui.NotificationEvent;
import org.jbpm.formbuilder.client.bus.ui.NotificationHandler;
import org.jbpm.formbuilder.client.resources.FormBuilderGlobals;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.shared.EventBus;

/**
 * Notifications presenter. Adds messages to
 * the view when a notification happens
 */
public class NotificationsPresenter {

    private final NotificationsView view;
    private final EventBus bus = FormBuilderGlobals.getInstance().getEventBus();

    public NotificationsPresenter(NotificationsView notifView) {
        this.view = notifView;
        bus.addHandler(NotificationEvent.TYPE, new NotificationHandler() {
            public void onEvent(NotificationEvent event) {
                String colorCss = view.getColorCss(event.getLevel().name());
                String message = event.getMessage();
                Throwable error = event.getError();
                view.append(colorCss, message, error);
            }
        });
        view.addFocusHandler(new FocusHandler() {
            public void onFocus(FocusEvent event) {
                view.saveHeight();
                view.setHeight("300px");
            }
        });
        view.addBlurHandler(new BlurHandler() {
            public void onBlur(BlurEvent event) {
                view.setHeight(view.getSavedHeight());
            }
        });
    }
    
}
