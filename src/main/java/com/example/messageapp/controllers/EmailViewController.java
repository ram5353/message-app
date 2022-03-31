package com.example.messageapp.controllers;


import com.example.messageapp.email.Email;
import com.example.messageapp.email.EmailRepository;
import com.example.messageapp.emailslist.EmailsListRepository;
import com.example.messageapp.folders.Folder;
import com.example.messageapp.folders.FolderRepository;
import com.example.messageapp.folders.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class EmailViewController {

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private FolderService folderService;

    @Autowired
    private EmailRepository emailRepository;

    @GetMapping("/emails/{id}")
    public String emailView(@AuthenticationPrincipal OAuth2User principal, Model model,
                            @PathVariable UUID id) {
        if (principal == null || !StringUtils.hasText(principal.getAttribute("login"))) {
            return "index";
        }

        // Fetch folders
        String userId = principal.getAttribute("login");
        List<Folder> userFolders = folderRepository.findAllById(userId);
        model.addAttribute("userFolders", userFolders);
        List<Folder> defaultFolders = folderService.fetchDefaultFolders(userId);
        model.addAttribute("defaultFolders", defaultFolders);

        Optional<Email> optionalEmail = emailRepository.findById(id);
        if (optionalEmail.isEmpty()) {
            return "inbox-page";
        }
        Email email = optionalEmail.get();
        String toIds = String.join(", ", email.getTo());
        model.addAttribute("email", optionalEmail.get());
        model.addAttribute("toIds", toIds);
        return "email-page";

    }

}
