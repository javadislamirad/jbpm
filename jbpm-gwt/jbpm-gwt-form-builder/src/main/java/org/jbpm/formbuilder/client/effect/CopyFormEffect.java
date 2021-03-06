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
package org.jbpm.formbuilder.client.effect;

import org.jbpm.formbuilder.client.resources.FormBuilderGlobals;

/**
 * Allows the copy action from the right click menu of
 * the related {@link FBFormItem}
 */
public class CopyFormEffect extends FBFormEffect {

    public CopyFormEffect() {
        super("Copy", false);
    }

    @Override
    protected void createStyles() {
        FormBuilderGlobals.getInstance().copy().append(getItem()).execute();
    }

}
