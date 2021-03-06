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
package org.jbpm.formbuilder.server.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jbpm.bpmn2.xml.UserTaskHandler;
import org.jbpm.formbuilder.shared.task.TaskPropertyRef;
import org.jbpm.formbuilder.shared.task.TaskRef;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class HumanTaskGetInformationHandler extends UserTaskHandler {

    private final TaskRepoHelper taskRepository;

    /**
     * Creates a new {@link HumanTaskGetInformationHandler} instance.
     * 
     * @param humanTaskRepository
     *            the {@link HumanTaskRepository}.
     */
    public HumanTaskGetInformationHandler(
            TaskRepoHelper taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * Reads the io specification and put the information in the
     * {@link HumanTaskRepository}.
     */
    @Override
    protected void readIoSpecification(org.w3c.dom.Node xmlNode,
            Map<String, String> dataInputs, Map<String, String> dataOutputs) {
        super.readIoSpecification(xmlNode, dataInputs, dataOutputs);
        NamedNodeMap map = xmlNode.getParentNode().getAttributes();
        Node nodeName = map.getNamedItem("name");
        String name = nodeName.getNodeValue();
        TaskRef task = new TaskRef();
        task.setTaskId(name);
        if (dataInputs != null) {
            List<TaskPropertyRef> inputs = new ArrayList<TaskPropertyRef>(dataInputs.size());
            for (Map.Entry<String, String> in : dataInputs.entrySet()) {
                TaskPropertyRef prop = new TaskPropertyRef();
                prop.setName(in.getKey());
                prop.setSourceExpresion(in.getValue());
            }
            task.setInputs(inputs);
        }
        if (dataOutputs != null) {
            List<TaskPropertyRef> outputs = new ArrayList<TaskPropertyRef>(dataOutputs.size());
            for (Map.Entry<String, String> out : dataOutputs.entrySet()) {
                TaskPropertyRef prop = new TaskPropertyRef();
                prop.setName(out.getKey());
                prop.setSourceExpresion(out.getValue());
            }
            task.setOutputs(outputs);
        }
        this.taskRepository.addTask(task);
    }

}
