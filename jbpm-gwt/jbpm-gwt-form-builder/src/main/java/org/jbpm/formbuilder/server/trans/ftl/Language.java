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
package org.jbpm.formbuilder.server.trans.ftl;

import org.jbpm.formbuilder.server.trans.LanguageException;
import org.jbpm.formbuilder.server.trans.LanguageFactory;
import org.jbpm.formbuilder.server.trans.ScriptingLanguage;
import org.jbpm.formbuilder.shared.rep.FBScript;

public class Language extends ScriptingLanguage {

    private static final String LANG = "ftl";

    public Language() {
        super(LANG, "/langs/freemarker/");
    }

    public String toServerScript(FBScript script) throws LanguageException {
        if (isValidScript(script)) {
            return asFtlScript(script);
        } else {
            throw new LanguageException(script.getType() + " is not a supported language");
        }
    }

    private String asFtlScript(FBScript script) {
        StringBuilder builder = new StringBuilder();
        if (script.getContent() != null && !"".equals(script.getContent())) {
            builder.append(script.getContent());
        } else if (script.getSrc() != null && !"".equals(script.getSrc())) {
            builder.append("<#include '").append(script.getSrc()).append("'>\n");
        }
        return builder.toString();
    }

    private boolean isValidScript(FBScript script) {
        return script != null && script.getType() != null && (script.getType().contains(LANG) || script.getType().contains("freemarker"));
    }
    
    public boolean isClientScript(FBScript script) {
        return LanguageFactory.getInstance().isClientSide(script.getType());
    }
}
