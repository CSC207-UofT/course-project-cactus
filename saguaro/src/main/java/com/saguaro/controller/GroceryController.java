package com.saguaro.controller;

import com.saguaro.entity.GroceryList;
import com.saguaro.exception.ResourceNotFoundException;
import com.saguaro.service.GroceryService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;

/**
 * REST controller defining endpoints related to grocery list management. The following
 * endpoints are defined:
 * <ul>
 *     <li>api/all-lists
 *     <li>api/list
 *     <li>api/create-list
 *     <li>api/save-list
 *     <li>api/delete-list
 * </ul>
 *
 * @author Charles Wong
 */
@RestController
@Validated
public class GroceryController {

    /**
     * A grocery list Service to delegate logic to.
     */
    private final GroceryService groceryService;

    /**
     * Constructs this grocery controller given a GroceryService.
     * <p>
     * Notice that by default, Spring will attempt to autowire the only
     * constructor of a class.
     *
     * @param groceryService a GroceryService to provide logic for this controller
     */
    public GroceryController(GroceryService groceryService) {
        this.groceryService = groceryService;
    }

    /**
     * Fetches all grocery lists for the currently authenticated user.
     * <p>
     * Since this endpoint is a protected resource, a valid username must
     * be available from the SecurityContext when this method is invoked.
     *
     * @return a Map from list IDs to list names
     */
    @GetMapping("api/all-lists")
    public Map<Long, String> getLists() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) auth.getPrincipal();

        return groceryService.getOwnedListNames(username);
    }

    /**
     * Fetch the details of a grocery list given a valid list ID. An invalid ID includes
     * both IDs which do not exist, or IDs for which the corresponding list does not
     * belong to the currently authenticated user. If such an ID is provided, a
     * ResourceNotFoundException will be thrown.
     * <p>
     * A successful fetch will return a GroceryList object, which in this context is
     * simply a vehicle for the following information:
     * <ul>
     *     <li>list ID
     *     <li>list name
     *     <li>grocery items
     * </ul>
     * <p>
     * Since this endpoint is a protected resource, a valid username must
     * be available from the SecurityContext when this method is invoked.
     *
     * @param id the ID specifying the grocery list to fetch
     * @return a GroceryList object which can be deserialized
     * @throws ResourceNotFoundException if the provided list ID is invalid
     */
    @GetMapping("api/list")
    public GroceryList getList(@RequestParam("id") long id) throws ResourceNotFoundException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) auth.getPrincipal();

        return groceryService.getListById(id, username);
    }

    /**
     * Create a new grocery list with the given name. Optionally mark the grocery list as
     * a template, or initialize the grocery list with the items from an existing template. Note that
     * these are mutually exclusive, with marking as template having precedence: if a grocery list is marked
     * as a template, it will not be initialized with an existing template, regardless of if that argument is
     * provided.
     * <p>
     * Returns a GroceryList object,
     * which in this context is simply a vehicle for the following information:
     * <ul>
     *      <li>list ID
     *      <li>list name
     *      <li>grocery items
     * </ul>
     *
     * @param name the String to set the new grocery list's name to
     * @return the newly created GroceryList object
     */
    @PostMapping("api/create-list")
    public GroceryList createNewList(@RequestParam("name") String name,
                                     @RequestParam(value = "template", required = false, defaultValue = "false") boolean template,
                                     @RequestParam(value = "templateId", required = false) Long templateId)
            throws ResourceNotFoundException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) auth.getPrincipal();

        if (template) {
            return groceryService.createNewList(name, username, template);
        } else if (templateId != null) {
            return groceryService.createNewList(name, username, templateId);
        }

        return groceryService.createNewList(name, username, template);
    }

    /**
     * Given some valid GroceryList object, saves the object as the new state of the
     * list. The grocery list to be overwritten is identified by the list ID. The
     * name attribute may be null, in which case the list's name will remain unchanged.
     * <p>
     * If the provided list has an ID which does not exist, or the currently authenticated
     * user does not own or have shared access to the list corresponding to the ID, a
     * ResourceNotFoundException is thrown.
     * <p>
     * If the save is successful, then the newly saved grocery list is returned.
     * <p>
     * Since this endpoint is a protected resource, a valid username must
     * be available from the SecurityContext when this method is invoked.
     *
     * @param list the GroceryList to save
     * @return the newly saved GroceryList
     * @throws ResourceNotFoundException if the provided GroceryList contains an invalid ID
     */
    @PutMapping("api/save-list")
    public GroceryList saveList(@Validated @RequestBody GroceryList list) throws ResourceNotFoundException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) auth.getPrincipal();

        return groceryService.saveList(list, username);
    }

    /**
     * Edit the name of some existing grocery list. The grocery list to edit is specified by a long representing
     * an ID, and a non-blank new name must be provided.
     * <p>
     * If the provided list ID does not match any grocery list belonging to the currently authenticated user,
     * a ResourceNotFoundException is thrown.
     * <p>
     * If the edit is successful, the newly modified grocery list is returned.
     *
     * @param name the String to set the list name to
     * @param id   a long representing the ID of the grocery list to edit
     * @return the newly modified GroceryList
     * @throws ResourceNotFoundException if the provided list ID does not match any grocery list belonging to
     *                                   the currently authenticated user
     */
    @PutMapping("api/edit-list-name")
    public GroceryList editListName(@RequestParam("name") @NotBlank String name, @RequestParam("id") long id)
            throws ResourceNotFoundException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) auth.getPrincipal();

        return groceryService.editListName(id, name, username);
    }

    /**
     * Given a valid list ID, delete the corresponding list. An invalid ID includes
     * both IDs which do not exist, or IDs for which the corresponding list does not
     * belong to the currently authenticated user. If such an ID is provided, a
     * ResourceNotFoundException will be thrown.
     *
     * @param id the ID of the list to delete
     * @throws ResourceNotFoundException if the provided list ID is invalid
     */
    @DeleteMapping("api/delete-list")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteList(@RequestParam("id") long id) throws ResourceNotFoundException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) auth.getPrincipal();

        groceryService.removeList(id, username);
    }

    /**
     * Fetch a comprehensive list of all the grocery lists the authenticated user has access to.
     * <p>
     * The returned JSON object has two top level properties:
     * <ul>
     *     <li>lists
     *     <li>templates
     * </ul>
     * <p>
     * The values of these are objects with the following two properties:
     * <ul>
     *     <li>owned
     *     <li>shared
     * </ul>
     * <p>
     * These properties correspond to an object mapping list ID to list name, of lists
     * that the authenticated user owns and has shared access to, respectively.
     *
     * @return a Map object describing all lists the authenticated user has access to
     */
    @GetMapping("api/v2/all-lists")
    public Map<String, Object> getAllListsFull() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) auth.getPrincipal();

        Map<String, Object> body = new HashMap<>();

        Map<String, Object> lists = new HashMap<>();
        lists.put("owned", groceryService.getOwnedListNames(username));
        lists.put("shared", groceryService.getSharedListNames(username));

        Map<String, Object> templates = new HashMap<>();
        templates.put("owned", groceryService.getOwnedTemplateNames(username));
        templates.put("shared", groceryService.getSharedTemplateNames(username));

        body.put("lists", lists);
        body.put("templates", templates);

        return body;
    }

    /**
     * Add a user to the shared users of a grocery list. A list can only be shared by the owner of the list,
     * and if the user it is being shared with is a friend of the owner. If any of these conditions are
     * not met, then a ResourceNotFoundException is thrown.
     * <p>
     * Furthermore, if the user to be shared with cannot be found, or if the list ID does not match any
     * existing list, a ResourceNotFoundException is thrown.
     * <p>
     * Since this endpoint is a protected resource, a valid username must be available from the SecurityContext
     * when this method is invoked.
     * <p>
     * If the sharing was successful, the newly modified GroceryList object is returned.
     *
     * @param id            a long representing the ID of the GroceryList to share
     * @param shareUsername the String username of the user to share the list with
     * @return the newly modified GroceryList object
     * @throws ResourceNotFoundException if the list to be shared does not belong to the sharer, or if the
     *                                   sharee is not a friend of the sharer
     */
    @PostMapping("api/share-list")
    public GroceryList shareList(@RequestParam("id") long id,
                                 @RequestParam("username") String shareUsername) throws ResourceNotFoundException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) auth.getPrincipal();

        return groceryService.shareList(id, shareUsername, username);
    }

    /**
     * Remove a user from the shared users of a grocery list. This operation can only be performed by the owner
     * of a list. In addition, only users that the list is already shared with can be "unshared" from a list. If
     * the above conditions are not met, then a ResourceNotFoundException will be thrown, with an appropriate
     * error message.
     * <p>
     * If the removal was successful, the newly modified GroceryList object is returned.
     *
     * @param id            a long representing the ID of the GroceryList to unshare
     * @param shareUsername the String username of the user to unshare the list with
     * @return the newly modified GroceryList object
     * @throws ResourceNotFoundException if the list to be unshared does not belong to the authenticated user, or
     *                                   if the user to be unshared was not part of the shared users of the list
     */
    @DeleteMapping("api/unshare-list")
    public GroceryList unshareList(@RequestParam("id") long id,
                                   @RequestParam("username") String shareUsername) throws ResourceNotFoundException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) auth.getPrincipal();

        return groceryService.unshareList(id, shareUsername, username);
    }
}
