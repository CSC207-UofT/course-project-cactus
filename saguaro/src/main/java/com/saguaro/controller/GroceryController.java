package com.saguaro.controller;

import com.saguaro.entity.GroceryList;
import com.saguaro.exception.ResourceNotFoundException;
import com.saguaro.service.GroceryService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
public class GroceryController {

    /**
     * A grocery list Service to delegate logic to.
     */
    private final GroceryService groceryService;

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

        return groceryService.getOwnedListNamesByUsername(username);
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
     * Create a new grocery list with the given name. Returns a GroceryList object,
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
    public GroceryList createNewList(@RequestParam("name") String name) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) auth.getPrincipal();

        return groceryService.createNewList(name, username);
    }

    /**
     * Given some valid GroceryList object, saves the object as the new state of the
     * list. The grocery list to be overwritten is identified by the list ID. The
     * name attribute may be null, in which case the list's name will remain unchanged.
     * <p>
     * If the provided list has an ID which does not exist, or the ID corresponds to a
     * list that does not belong to the currently authenticated user, a
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
     *
     * The returned JSON object has two top level properties:
     * <ul>
     *     <li>lists
     *     <li>templates
     * </ul>
     *
     * The values of these are objects with the following two properties:
     * <ul>
     *     <li>owned
     *     <li>shared
     * </ul>
     *
     * These properties correspond to an object mapping list ID to list name, of lists
     * that the authenticated user owns and has shared access to, respectively.
     *
     * @return a Map object describing all lists the authenticated user has access to
     */
    @GetMapping("api/v2/all-lists")
    public Map<String, Object> shareList() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) auth.getPrincipal();

        Map<String, Object> body = new HashMap<>();

        Map<String, Object> lists = new HashMap<>();
        lists.put("owned", groceryService.getOwnedListNamesByUsername(username));
        lists.put("shared", groceryService.getSharedListNamesByUsername(username));

        Map<String, Object> templates = new HashMap<>();
        // TODO: get templates from service
        lists.put("owned", new HashMap<>());
        lists.put("shared", new HashMap<>());

        body.put("lists", lists);
        body.put("templates", templates);

        return body;
    }
}
