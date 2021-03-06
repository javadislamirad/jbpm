
package org.jbpm.formbuilder.client;

import java.util.List;
import java.util.Map;

import org.jbpm.formbuilder.client.menu.FBMenuItem;
import org.jbpm.formbuilder.client.options.MainMenuOption;
import org.jbpm.formbuilder.client.validation.FBValidationItem;
import org.jbpm.formbuilder.shared.rep.FormItemRepresentation;
import org.jbpm.formbuilder.shared.rep.FormRepresentation;
import org.jbpm.formbuilder.shared.rep.RepresentationFactory;
import org.jbpm.formbuilder.shared.task.TaskRef;

/**
 * Client's interface with the REST API server.
 */
public interface FormBuilderService {

    /**
     * Gets a map of groups indexed by group name. Each group contains a list of menu items available in each group.
     * @return a map of groups with its lists of menu items.
     * @throws FormBuilderException in case of error.
     */
    Map<String, List<FBMenuItem>> getMenuItems() throws FormBuilderException;
    
    /**
     * Gets a list of menu options, generally for a menu bar. Each option has a name, and 
     * maybe a subMenu consisting of a list of menu options, or maybe a command, or maybe both.
     * @return a list of menu options.
     * @throws FormBuilderException in case of error.
     */
    List<MainMenuOption> getMenuOptions() throws FormBuilderException;
    
    /**
     * Saves a form on the server
     * @param form The form to be saved
     * @throws FormBuilderException in case of error
     */
    void saveForm(FormRepresentation form) throws FormBuilderException;
    
    /**
     * Saves a UI component on the server
     * @param formItem the UI component to be saved
     * @param formItemName the UI component name
     * @throws FormBuilderException in case of error
     */
    void saveFormItem(final FormItemRepresentation formItem, String formItemName) throws FormBuilderException;
    
    /**
     * Deletes a form from the server
     * @param form The form to be deleted
     * @throws FormBuilderException in case of error
     */
    void deleteForm(FormRepresentation form) throws FormBuilderException;
    
    /**
     * Deletes a UI component from the server
     * @param formItemName The UI component name
     * @param formItem the UI component to be deleted
     * @throws FormBuilderException in case of error
     */
    void deleteFormItem(String formItemName, final FormItemRepresentation formItem) throws FormBuilderException;
    
    /**
     * Translates a form. An event exposes where to retrieve the form from.
     * 
     * @param form Form to be translated
     * @param language Language to translate the form
     * @throws FormBuilderException in case of error
     */
    void generateForm(FormRepresentation form, String language, Map<String, Object> inputs) throws FormBuilderException;
    
    /**
     * Saves a new (custom) menu item on the server
     * @param groupName Group name of the new menu item
     * @param item The new menu item to be saved
     * @throws FormBuilderException in case of error
     */
    void saveMenuItem(String groupName, FBMenuItem item) throws FormBuilderException;
    
    /**
     * Deletes a custom menu item from the server
     * @param groupName Group name of the custom menu item to be deleted
     * @param item The custom menu item to be deleted
     * @throws FormBuilderException in case of error
     */
    void deleteMenuItem(String groupName, FBMenuItem item) throws FormBuilderException;
    
    /**
     * Returns the tasks as matching on a simple string filter
     * @param filter a filter for a google-like search textfield
     * @return all best fit task definition references to the filter
     * @throws FormBuilderException in case of error
     */
    List<TaskRef> getExistingTasks(String filter) throws FormBuilderException;

    /**
     * Returns existing validations from the server
     * @return all existing validation types available on server side
     * @throws FormBuilderException in case of error
     */
    List<FBValidationItem> getExistingValidations() throws FormBuilderException;

    /**
     * Returns a single form
     * @param formName the name of the form to be returned
     * @return the requested form
     * @throws FormBuilderException in case of error.
     */
    FormRepresentation getForm(String formName) throws FormBuilderException;
    
    /**
     * Returns all forms
     * @return all existing forms
     * @throws FormBuilderException in case of error.
     */
    List<FormRepresentation> getForms() throws FormBuilderException;
    
    /**
     * Populates the {@link RepresentationFactory} with the form items and representations
     * that belong to them.
     * @throws FormBuilderException in case of error.
     */
    void populateRepresentationFactory() throws FormBuilderException;

    /**
     * Loads a file from the server that contains a given language's form template
     * @param form the form representation to create a template from
     * @param language the result template expected language
     * @throws FormBuilderException in case of error.
     */
    void loadFormTemplate(FormRepresentation form, String language) throws FormBuilderException;
}
