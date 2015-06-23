package sk.tomsik68.mclauncher.backend;


public final class ProfileSelectionException extends Exception {
    ProfileSelectionException(String message){
        super(message);
    }
    ProfileSelectionException(){
        this("There are more profiles. Please specify profile name to be used for authentication.");
    }
}
