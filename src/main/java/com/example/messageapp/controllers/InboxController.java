package com.example.messageapp.controllers;


import com.example.messageapp.emailslist.EmailsList;
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

import java.util.List;

@Controller
public class InboxController {

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private FolderService folderService;

    @Autowired
    private EmailsListRepository emailsListRepository;

    @GetMapping("/")
    public String homePage(@AuthenticationPrincipal OAuth2User principal, Model model) {
        if (principal == null || !StringUtils.hasText(principal.getAttribute("login"))) {
            return "index";
        }

        // Fetch folders
        String userId = principal.getAttribute("login");
        List<Folder> userFolders = folderRepository.findAllById(userId);
        model.addAttribute("userFolders", userFolders);
        List<Folder> defaultFolders = folderService.fetchDefaultFolders(userId);
        model.addAttribute("defaultFolders", defaultFolders);

        // Fetch Messages
        String folderLabel = "Inbox";
        List<EmailsList> emailsList = emailsListRepository.findAllByKey_UserIdAndKey_Label(userId, folderLabel);
        model.addAttribute("emailsList", emailsList);
        return "inbox-page";
    }
}
