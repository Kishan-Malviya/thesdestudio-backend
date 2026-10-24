package com.thesdestudio.personal;

import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/me/subtopics")
public class PersonalNotesController {
    private final PersonalNotesService personalNotesService;

    public PersonalNotesController(PersonalNotesService personalNotesService) {
        this.personalNotesService = personalNotesService;
    }

    @PutMapping("/{subtopicId}/notes")
    public PersonalNotesResponse updateNotes(
            @PathVariable UUID subtopicId,
            @Valid @RequestBody UpdatePersonalNotesRequest request,
            @AuthenticationPrincipal OidcUser oidcUser
    ) {
        UserSubtopic userSubtopic = personalNotesService.updateNotes(
                oidcUser.getSubject(),
                subtopicId,
                request.notes()
        );
        return PersonalNotesResponse.from(userSubtopic);
    }

    @PutMapping("/{subtopicId}/visited")
    public VisitedStatusResponse updateVisited(
            @PathVariable UUID subtopicId,
            @Valid @RequestBody UpdateVisitedRequest request,
            @AuthenticationPrincipal OidcUser oidcUser
    ) {
        UserSubtopic userSubtopic = personalNotesService.updateVisited(
                oidcUser.getSubject(),
                subtopicId,
                request.visited()
        );
        return VisitedStatusResponse.from(userSubtopic);
    }

    @PutMapping("/{subtopicId}/revisit-later")
    public RevisitLaterResponse updateRevisitLater(
            @PathVariable UUID subtopicId,
            @Valid @RequestBody UpdateRevisitLaterRequest request,
            @AuthenticationPrincipal OidcUser oidcUser
    ) {
        UserSubtopic userSubtopic = personalNotesService.updateRevisitLater(
                oidcUser.getSubject(),
                subtopicId,
                request.revisitLater()
        );
        return RevisitLaterResponse.from(userSubtopic);
    }

    @GetMapping("/visited")
    public List<PersonalSubtopicResponse> getVisited(@AuthenticationPrincipal OidcUser oidcUser) {
        return personalNotesService.getVisited(oidcUser.getSubject());
    }

    @GetMapping("/revisit-later")
    public List<PersonalSubtopicResponse> getRevisitLater(@AuthenticationPrincipal OidcUser oidcUser) {
        return personalNotesService.getRevisitLater(oidcUser.getSubject());
    }
}
