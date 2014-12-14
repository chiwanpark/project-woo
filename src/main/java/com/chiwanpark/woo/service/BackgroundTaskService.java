package com.chiwanpark.woo.service;

import com.chiwanpark.woo.view.InProgressDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.SuccessCallback;

import javax.swing.*;
import java.util.concurrent.Callable;

@Service
public class BackgroundTaskService {
  private static final Logger LOG = LoggerFactory.getLogger(BackgroundTaskService.class);

  private @Autowired ApplicationContext context;

  @Async
  public <V> void runTaskInBackground(Callable<V> logic, final SuccessCallback<V> successCallback, final FailureCallback failureCallback) {
    LOG.info(String.format("Run task in background! <%s>", logic));

    try {
      final V output = logic.call();
      SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
          try {
            successCallback.onSuccess(output);
          } catch (Exception e) {
            failureCallback.onFailure(e);
          }
        }
      });
    } catch (final Exception e) {
      LOG.error("Exception caught while running task in background");

      SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
          failureCallback.onFailure(e);
        }
      });
    }
  }

  @Async
  public <V> void runTaskInBackgroundWithDialog(Callable<V> logic, final SuccessCallback<V> successCallback, final FailureCallback failureCallback) {
    final InProgressDialog dialog = context.getBean(InProgressDialog.class);

    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        dialog.setVisible(true);
      }
    });

    SuccessCallback<V> newSuccessCallback = new SuccessCallback<V>() {
      @Override
      public void onSuccess(V v) {
        if (dialog.isVisible()) {
          dialog.setModal(false);
          dialog.dispose();
        }

        successCallback.onSuccess(v);
      }
    };

    FailureCallback newFailureCallback = new FailureCallback() {
      @Override
      public void onFailure(Throwable e) {
        if (dialog.isVisible()) {
          dialog.setModal(false);
          dialog.dispose();
        }

        failureCallback.onFailure(e);
      }
    };

    runTaskInBackground(logic, newSuccessCallback, newFailureCallback);
  }
}
