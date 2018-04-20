/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.email.internal.util;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.mule.runtime.api.metadata.MediaType;
import org.mule.runtime.core.api.message.ds.InputStreamDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;

import javax.activation.DataSource;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.junit.Test;

public class TimeAfterFunctionTestCase {

  private static final LocalDateTime LOWER_BOUND = LocalDateTime.of(1983, 4, 20, 21, 15);
  private static final LocalDateTime UPPER_BOUND = LocalDateTime.of(2012, 3, 7, 18, 45);

  private TimeUntilFunction function = new TimeUntilFunction();

  @Test
  public void isBefore() {
    assertThat(function.apply(LOWER_BOUND, UPPER_BOUND), is(false));
  }

  @Test
  public void isAfter() {
    assertThat(function.apply(UPPER_BOUND, LOWER_BOUND), is(true));
  }
}
