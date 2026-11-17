package com.nicodev.iwanit.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.nicodev.iwanit.model.Role;
import com.nicodev.iwanit.model.User;

/**
 * Gives access to the user of the current request, as set by the
 * JwtAuthenticationFilter.
 */
@Component
public class AuthenticatedUser {

  /**
   * Checks whether the authenticated user acts with the given role and is the
   * owner of a resource, e.g. the buyer of an article or the seller of an offer.
   *
   * @param role  the role the user must have
   * @param owner the user that owns the resource
   * @return true if the authenticated user has the role and is the owner
   * @throws AccessDeniedException if there is no authenticated user
   */
  public boolean isOwner(Role role, User owner) {
    return hasRole(role) && owner != null && get().getId().equals(owner.getId());
  }

  /**
   * @param role the role the user must have
   * @return true if the authenticated user has the given role
   * @throws AccessDeniedException if there is no authenticated user
   */
  public boolean hasRole(Role role) {
    return get().getRole() == role;
  }

  /**
   * @return the authenticated user
   * @throws AccessDeniedException if there is no authenticated user
   */
  public User get() {
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !(authentication.getPrincipal() instanceof User user)) {
      throw new AccessDeniedException("Authentication required");
    }
    return user;
  }
}
