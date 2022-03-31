package com.example.messageapp.controllers;


import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.example.messageapp.emailslist.EmailsList;
import com.example.messageapp.emailslist.EmailsListRepository;
import com.example.messageapp.folders.Folder;
import com.example.messageapp.folders.FolderRepository;
import com.example.messageapp.folders.FolderService;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
public class InboxController {

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private FolderService folderService;

    @Autowired
    private EmailsListRepository emailsListRepository;

    private PrettyTime p = new PrettyTime();

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
        emailsList.stream().forEach(email -> {
            UUID timeUuid = email.getKey().getTimeId();
            Date emailDateTime = new Date(Uuids.unixTimestamp(timeUuid));
            email.setAgoTimeString(p.format(emailDateTime));
        });

        model.addAttribute("emailsList", emailsList);
        return "inbox-page";
    }
}
