package org.wahlzeit.model;

import org.junit.Test;
import org.mockito.MockedConstruction;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CGIPhotoTest {

    @Test
    public void testCGIPhotoHasEmptySoftwareNameByDefault() {
        CGIPhoto photo = new CGIPhoto();
        assertTrue(photo.getSoftwareName().isEmpty());
    }

    @Test
    public void testConstructCGIPhotoFromPhotoWithSoftwareName() {
        Photo photo = new Photo();
        photo.setOwnerName("ownerName");
        CGIPhoto cgiPhoto = new CGIPhoto(photo, "blender");
        assertTrue(photo.id == cgiPhoto.id);
        assertTrue(photo.ownerId == cgiPhoto.ownerId);
        assertTrue(photo.ownerName.equals(cgiPhoto.ownerName));
        assertTrue(photo.ownerNotifyAboutPraise == cgiPhoto.ownerNotifyAboutPraise);
        assertTrue(photo.ownerEmailAddress == cgiPhoto.ownerEmailAddress);
        assertTrue(photo.ownerLanguage == cgiPhoto.ownerLanguage);
        assertTrue(photo.ownerHomePage == cgiPhoto.ownerHomePage);
        assertTrue(photo.width == cgiPhoto.width);
        assertTrue(photo.height == cgiPhoto.height);
        assertTrue(photo.maxPhotoSize == cgiPhoto.maxPhotoSize);
        assertTrue(photo.tags == cgiPhoto.tags);
        assertTrue(photo.location == cgiPhoto.location);
        assertTrue(photo.status == cgiPhoto.status);
        assertTrue(photo.praiseSum == cgiPhoto.praiseSum);
        assertTrue(photo.noVotes == cgiPhoto.noVotes);
        assertTrue(photo.creationTime == cgiPhoto.creationTime);
        assertTrue(cgiPhoto.getSoftwareName().equals("blender"));
    }

    // Tried to use inline MockedConstruction as per
    // https://stackoverflow.com/questions/11214136/unit-testing-with-mockito-for-constructors
    // but couldn't get it to work after trying out many approaches for way to long...
//    @Test
//    public void testConstructCGIPhotoFromPhoto() {
//        Photo photo = new Photo();
//        try (MockedConstruction mocked = mockConstruction(CGIPhoto.class)) {
//            CGIPhoto cgiPhoto = new CGIPhoto(photo);
//            when(cgiPhoto.readSoftwareNameFromDBForID(anyInt())).thenReturn("<softwareName>");
//            verify(cgiPhoto).copyPhotoFields(photo);
//            assertEquals("<softwareName>", cgiPhoto.readSoftwareNameFromDBForID(1));
//            verify(cgiPhoto).readSoftwareNameFromDBForID(photo.getId().asInt());
//        }
//    }
}
