package com.teamtreehouse.giflib.web.controller;

import com.teamtreehouse.giflib.model.Gif;
import com.teamtreehouse.giflib.service.CategoryService;
import com.teamtreehouse.giflib.service.GifService;
import com.teamtreehouse.giflib.web.FlashMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class GifController {
    @Autowired
    private GifService gifService;
    @Autowired
    private CategoryService categoryService;
    // Home page - index of all GIFs
    @RequestMapping("/")
    public String listGifs(Model model) {
        // TODO: Get all gifs
        List<Gif> gifs = gifService.findAll();

        model.addAttribute("gifs", gifs);
        return "gif/index";
    }

    // Single GIF page
    @RequestMapping("/gifs/{gifId}")
    public String gifDetails(@PathVariable Long gifId, Model model) {
        // TODO: Get gif whose id is gifId
        Gif gif = gifService.findById(gifId);

        model.addAttribute("gif", gif);
        return "gif/details";
    }

    // GIF image data
    @RequestMapping("/gifs/{gifId}.gif")
    @ResponseBody//means thymeleaf won't render view but will use return value below.
    public byte[] gifImage(@PathVariable Long gifId) {
        // TODO: Return image data as byte array of the GIF whose id is gifId
        return gifService.findById(gifId).getBytes();
    }

    // Favorites - index of all GIFs marked favorite
    @RequestMapping("/favorites")
    public String favorites(Model model) {
        // TODO: Get list of all GIFs marked as favorite
        List<Gif> faves = new ArrayList<>();

        model.addAttribute("gifs",faves);
        model.addAttribute("username","Chris Ramacciotti"); // Static username
        return "gif/favorites";
    }

    // Upload a new GIF
    @RequestMapping(value = "/gifs", method = RequestMethod.POST)
    public String addGif(@Valid Gif gif, @RequestParam MultipartFile file, BindingResult result,
                         RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            //Include validation errors upon redirect
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult" +
                    ".gif", result);
            //add category if invalid was received
            redirectAttributes.addFlashAttribute("gif", gif);


            //Redirect back to the form
            return "redirect:/categories/add";
        }


        // TODO: Upload new GIF if data is valid
        gifService.save(gif,file);

            //add flash message for success
            redirectAttributes.addFlashAttribute("flash", new FlashMessage("Gif successfully " +
                    "uploaded!", FlashMessage.Status.SUCCESS));


            // TODO: Redirect browser to new GIF's detail view
            return String.format("redirect:/gifs/%s", gif.getId());
        }



    // Form for uploading a new GIF
    @RequestMapping("/upload")
    public String formNewGif(Model model) {
        // TODO: Add model attributes needed for new GIF upload form
        model.addAttribute("gif",new Gif());
        model.addAttribute("categories",categoryService.findAll());
        model.addAttribute("action", "/gifs");
        model.addAttribute("heading", "New Gif");
        model.addAttribute("submit", "Add");

        return "gif/form";
    }

    // Form for editing an existing GIF
    @RequestMapping(value = "/gifs/{gifId}/edit")
    public String formEditGif(@PathVariable Long gifId, Model model) {
        // TODO: Add model attributes needed for edit form
        if(!model.containsAttribute("gif" )){
            //this is different than formNewCategory, not new Category but findById passed into
            // method.
            model.addAttribute("gif", gifService.findById(gifId));
        }
        model.addAttribute("action", String.format("/gifs/%s",gifId));
        model.addAttribute("heading", "Edit Category");
        model.addAttribute("submit", "Update");

        return "gif/form";
    }

    // Update an existing GIF
    @RequestMapping(value = "/gifs/{gifId}", method = RequestMethod.POST)
    public String updateGif(@Valid Gif gif, @RequestParam MultipartFile file, BindingResult result
            , RedirectAttributes redirectAttributes) {
        // TODO: Update GIF if data is valid
        if (result.hasErrors()) {
            //Include validation errors upon redirect
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult" +
                    ".gif", result);
            //add category if invalid was received
            redirectAttributes.addFlashAttribute("gif", gif);


            //Redirect back to the form
            return String.format("redirect:/categories/%s/edit",gif.getId());
        }
        gifService.save(gif,file);

        redirectAttributes.addFlashAttribute("flash", new FlashMessage("Gif Successfully " +
                "updated!", FlashMessage.Status.SUCCESS));
        // TODO: Redirect browser to /categories
        return "redirect:/gifs";
    }

    // Delete an existing GIF
    @RequestMapping(value = "/gifs/{gifId}/delete", method = RequestMethod.POST)
    public String deleteGif(@PathVariable Long gifId, RedirectAttributes redirectAttributes) {
        // TODO: Delete the GIF whose id is gifId
        Gif gif = gifService.findById(gifId);
        gifService.delete(gif);

        redirectAttributes.addFlashAttribute("flash",new FlashMessage("Gif deleted.",
                FlashMessage.Status.SUCCESS));

        // TODO: Redirect to app root
        return "redirect:/";
    }

    // Mark/unmark an existing GIF as a favorite
    @RequestMapping(value = "/gifs/{gifId}/favorite", method = RequestMethod.POST)
    public String toggleFavorite(@PathVariable Long gifId) {
        // TODO: With GIF whose id is gifId, toggle the favorite field

        // TODO: Redirect to GIF's detail view
        return null;
    }

    // Search results
    @RequestMapping("/search")
    public String searchResults(@RequestParam String q, Model model) {
        // TODO: Get list of GIFs whose description contains value specified by q
        List<Gif> gifs = new ArrayList<>();

        model.addAttribute("gifs",gifs);
        return "gif/index";
    }
}