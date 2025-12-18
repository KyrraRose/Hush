package org.pluralsight.data;


import org.pluralsight.models.Profile;

public interface ProfileDao
{
    Profile getById(int userId);
    Profile create(Profile profile);
}
