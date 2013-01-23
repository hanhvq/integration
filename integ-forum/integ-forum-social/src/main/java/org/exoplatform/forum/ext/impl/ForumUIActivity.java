package org.exoplatform.forum.ext.impl;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.exoplatform.forum.ext.activity.ForumActivityBuilder;
import org.exoplatform.forum.service.Utils;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.social.core.activity.model.ExoSocialActivity;
import org.exoplatform.social.webui.activity.BaseUIActivity;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.lifecycle.UIFormLifecycle;
import org.exoplatform.webui.core.lifecycle.WebuiBindingContext;

@ComponentConfig(lifecycle = UIFormLifecycle.class, template = "classpath:groovy/forum/social-integration/plugin/space/ForumUIActivity.gtmpl", events = {
    @EventConfig(listeners = BaseUIActivity.ToggleDisplayLikesActionListener.class),
    @EventConfig(listeners = BaseUIActivity.ToggleDisplayCommentFormActionListener.class),
    @EventConfig(listeners = BaseUIActivity.LikeActivityActionListener.class),
    @EventConfig(listeners = BaseUIActivity.SetCommentListStatusActionListener.class),
    @EventConfig(listeners = BaseUIActivity.PostCommentActionListener.class),
    @EventConfig(listeners = BaseUIActivity.DeleteActivityActionListener.class, confirm = "UIActivity.msg.Are_You_Sure_To_Delete_This_Activity"),
    @EventConfig(listeners = BaseUIActivity.DeleteCommentActionListener.class, confirm = "UIActivity.msg.Are_You_Sure_To_Delete_This_Comment") })
public class ForumUIActivity extends BaseKSActivity {

  private static final Log LOG = ExoLogger.getLogger(ForumUIActivity.class);

  public ForumUIActivity() {
  }

  /*
   * used by template, see line 201 ForumUIActivity.gtmpl
   */
  @SuppressWarnings("unused")
  private String getReplyLink() {
    String viewLink = getActivityParamValue(ForumActivityBuilder.TOPIC_LINK_KEY);
    
    StringBuffer sb = new StringBuffer(viewLink);
    if (sb.lastIndexOf("/") == -1 || sb.lastIndexOf("/") != sb.length() - 1) {
      sb.append("/");
    }
    // add signal to show reply form
    sb.append("false");
    return sb.toString();
  }

  private String getLink(String tagLink, String nameLink) {
    String viewLink = getActivityParamValue(ForumActivityBuilder.TOPIC_LINK_KEY);
    return String.format(tagLink, viewLink, nameLink);
  }
  
  public String getViewLink() {
    String link = getActivityParamValue(ForumActivityBuilder.TOPIC_LINK_KEY);
    return link;
  }

  /*
   * used by Template, line 160 ForumUIActivity.gtmpl
   */
  @SuppressWarnings("unused")
  private String getActivityContentTitle(WebuiBindingContext _ctx, String herf) throws Exception {
    String title = getActivity().getTitle();
    String linkTag = "";
    try {
      linkTag = getLink(herf, getActivityParamValue(ForumActivityBuilder.TOPIC_NAME_KEY));
    } catch (Exception e) { // WebUIBindingContext
      LOG.debug("Failed to get activity content and title ", e);
    }
    return linkTag;
  }
  
  public String getNumberOfReplies() {
    ExoSocialActivity activity = getActivity();
    Map<String, String> templateParams = activity.getTemplateParams();
    
    String got = templateParams.get(ForumActivityBuilder.TOPIC_POST_COUNT_KEY);
    return String.format("%s Replies", got);
  }
  
  public String getRate() {
    ExoSocialActivity activity = getActivity();
    Map<String, String> templateParams = activity.getTemplateParams();
    
    String got = templateParams.get(ForumActivityBuilder.TOPIC_VOTE_RATE_KEY);
    return String.format("Rate: %s", got);
  }

}
