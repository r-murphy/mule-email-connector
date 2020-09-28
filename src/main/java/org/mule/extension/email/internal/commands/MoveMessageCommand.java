package org.mule.extension.email.internal.commands;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;

import org.mule.extension.email.api.exception.EmailAccessingFolderException;
import org.mule.extension.email.api.exception.EmailNotFoundException;
import org.mule.extension.email.internal.mailbox.MailboxConnection;

import com.sun.mail.imap.IMAPFolder;

import static java.lang.String.format;
import static javax.mail.Folder.READ_WRITE;

/**
 * Represents the move operation.
 */
public class MoveMessageCommand {

  /**
    * Moves an email message. The message will be moved to the target folder.
    * @param connection             the connection associated to the operation.
    * @param sourceFolderName       the name of folder containing the message.
    * @param destinationFolderName  the name of folder to move the message.
    * @param emailId                the id of the email to move
    */
  public void move(MailboxConnection connection,
                    String sourceFolderName,
                    String destinationFolderName,
                    long emailId)
    throws MessagingException {

    // Whichever folder is opened by the connection first will get closed,
    //  so using our own getFolder helper that also skips caching the folder.
    final Folder destinationFolder = getFolder(connection.getStore(), destinationFolderName);

    try {
      Folder sourceFolder = connection.getFolder(sourceFolderName, READ_WRITE);
      IMAPFolder imapSourceFolder = (IMAPFolder) sourceFolder;
      Message message = imapSourceFolder.getMessageByUID(emailId);
      if (message == null) {
        throw new EmailNotFoundException(format("No email was found with id: [%s]", emailId));
      }
      Message[] messages = {message};
      imapSourceFolder.moveMessages(messages, destinationFolder);
    } finally {
      closeQuietly(destinationFolder);
    }
  }

  private Folder getFolder(Store store, String name) {
    try {
      Folder folder = store.getFolder(name);
      folder.open(READ_WRITE);
      return folder;
    } catch (MessagingException e) {
      throw new EmailAccessingFolderException(format("Error while opening folder %s", name), e);
    }
  }

  private void closeQuietly(Folder folder) {
    try {
      if (folder != null && folder.isOpen()) {
        folder.close(false);
      }
    } catch (Exception ignore) {}
  }

}
