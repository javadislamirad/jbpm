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
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.jbpm.formbuilder.client.bus.ui.FormItemAddedEvent;
import org.jbpm.formbuilder.client.bus.ui.FormItemRemovedEvent;
import org.jbpm.formbuilder.client.effect.FBFormEffect;
import org.jbpm.formbuilder.client.form.FBCompositeItem;
import org.jbpm.formbuilder.client.form.FBFormItem;
import org.jbpm.formbuilder.client.resources.FormBuilderGlobals;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

/**
 * base UI form item. Represents a composite of {@link FBFormItem} instances
 */
public abstract class LayoutFormItem extends FBFormItem implements FBCompositeItem {

    private List<FBFormItem> items = new ArrayList<FBFormItem>();

    public LayoutFormItem() {
        this(new ArrayList<FBFormEffect>());
    }
    
    public LayoutFormItem(List<FBFormEffect> formEffects) {
        super(formEffects);
    }
    
    public List<FBFormItem> getItems() {
        return items;
    }
    
    public boolean removeItem(FBFormItem item) {
    	return this.items.remove(item);
    }
    
    public void setItems(List<FBFormItem> items) {
        this.items = items;
    }

    public int size() {
        return items.size();
    }

    public Iterator<FBFormItem> formItemIterator() {
        return items.iterator();
    }

    @Override
    public boolean remove(Widget w) {
        EventBus bus = FormBuilderGlobals.getInstance().getEventBus();
        if (w instanceof FBFormItem) {
            FBFormItem item = (FBFormItem) w;
            this.items.remove(item);
            bus.fireEvent(new FormItemRemovedEvent(item));
        }
        return super.remove(w);
    }
    
    public boolean remove(Object o) {
        return items.remove(o);
    }

    public FBFormItem get(int index) {
        return items.get(index);
    }

    public FBFormItem remove(int index) {
        return items.remove(index);
    }
    
    public ListIterator<FBFormItem> formItemListIterator() {
        return items.listIterator();
    }

    public boolean add(FBFormItem item) {
        boolean add = items.add(item);
        if (item != null) {
            EventBus bus = FormBuilderGlobals.getInstance().getEventBus();
            bus.fireEvent(new FormItemAddedEvent(item, this));
        }
        return add;
    }

    public FBFormItem insert(int index, FBFormItem newItem) {
        FBFormItem item = null;
        if (items.get(index) == null || isWhiteSpace(items.get(index))) {
            item = items.set(index, newItem);
        } else {
            item = newItem;
            for (; index < items.size() ; index++) {
                item = items.set(index, item);
            }
            items.add(item);
        }
        if (item != null) {
            EventBus bus = FormBuilderGlobals.getInstance().getEventBus();
            bus.fireEvent(new FormItemAddedEvent(newItem, this));
        }
        return item;
    }

    protected boolean isWhiteSpace(Widget widget) {
        return widget.getElement().getParentElement().getInnerHTML().equals("&nbsp;");
    }
    
    public abstract Panel getPanel();
    
    public Panel getUnderlyingLayout(int x, int y) {
        for (FBFormItem item : items) {
            if (item instanceof LayoutFormItem) {
                Panel newLayout = ((LayoutFormItem) item).getUnderlyingLayout(x, y);
                if (newLayout != null) {
                    return newLayout;
                }
            }
        }
        if (x > getAbsoluteLeft() && x < getAbsoluteLeft() + getOffsetWidth() &&
            y > getAbsoluteTop() && y < getAbsoluteTop() + getOffsetHeight()) {
            return getPanel();
        }
        return null;
    }
}
