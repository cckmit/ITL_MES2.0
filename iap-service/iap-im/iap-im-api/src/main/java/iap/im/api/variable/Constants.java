/*
 * Copyright ? 2017 海通安恒科技有限公司.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ***************************************************************************************
 *                                                                                     *
 *                        Website : http://www.htah.com.cn/                            *
 *                                                                                     *
 ***************************************************************************************
 */
package iap.im.api.variable;

/**
 * IM常量
 *
 * @author tanq
 * @date 2020-10-10
 * @since jdk1.8
 */
public interface Constants {

    String SYSTEM = "system";

    String SYSTEM_AVATAR = "https://pic.downk.cc/item/5f587231160a154a67e10987.png";

    String DEF_PASSWORD = "000000";

    long MAX_PUB_ROOT_MENU = 3;

    long MAX_PUB_SUB_MENU = 5;

    String LOCAL_BUCKET = "bucket";

    int PAGE_SIZE = 20;

    int API_PAGE_SIZE = 20;


    String REQ_PATH_HEADER = "req_source";
    String REQ_PATH_API = "api";

    interface MessageAction {

        // 用户之间的普通消息
        String ACTION_0 = "0";

        String ACTION_1 = "1";

        // 系统向用户发送的普通消息
        String ACTION_2 = "2";

        // 群里用户发送的 消息
        String ACTION_3 = "3";

        /*
         * ********************************************1开头统一为聊天消息**********************************************************
         */

        // 系统定制消息---消息被撤回
        String ACTION_101 = "101";

        // 系统定制消息---创建群
        String ACTION_102 = "102";

        // 系统定制消息---邀请进入群
        String ACTION_103 = "103";

        // 系统定制消息---群解散消息
        String ACTION_104 = "104";

        // 系统定制消息---踢出群
        String ACTION_105 = "105";

        // 系统定制消息---同意邀请入群请求
        String ACTION_106 = "106";

        // 系统定制消息---被剔除群
        String ACTION_107 = "107";

        // 系统定制消息---消息被阅读
        String ACTION_108 = "108";

        // 系统定制消息---被好友删除
        String ACTION_109 = "109";

        // 系统定制消息---好友替换了头像
        String ACTION_110 = "110";

        // 系统定制消息---好友修改了名称或者签名
        String ACTION_111 = "111";

        // 系统定制消息---用户退出了群
        String ACTION_112 = "112";

        // 系统定制消息---用户加入了群
        String ACTION_113 = "113";

        // 系统定制消息---群名称被修改
        String ACTION_114 = "114";

        // 系统定制消息---群公告被修改
        String ACTION_115 = "115";

        // 系统定制消息---群logo被修改
        String ACTION_116 = "116";
        // 系统定制消息---系统自动回复消息
        String ACTION_118 = "118";

        /*
         * ********************************************2开头统一为公众号消息**********************************************************
         */
        // 系统定制消息---公众号向用户回复的消息
        String ACTION_201 = "201";

        // 系统定制消息---公众号向用户群发消息
        String ACTION_202 = "202";

        // 系统定制消息---公众号信息更新
        String ACTION_203 = "203";

        // 系统定制消息---公众号菜单信息更新
        String ACTION_204 = "204";

        // 系统定制消息---公众号LOGO更新
        String ACTION_205 = "205";

        // 系统定制消息---公众号被删除
        String ACTION_206 = "206";

        /*
         * ********************************************2开头统一为小程序消息**********************************************************
         */


        // 系统定制消息---后台设置小程序Logo
        String ACTION_300 = "300";

        // 系统定制消息---后台新增或者修改了小程序
        String ACTION_301 = "301";

        // 系统定制消息---后台删除了小程序
        String ACTION_302 = "302";
        /*
         ********************************************* 4开头统一为系统控制消息**********************************************************
         */
        // 系统定制消息---强制下线消息
        String ACTION_444 = "444";


        //系统定制消息---清除聊天记录
        String ACTION_403 = "403";

        /*
         * ********************************************5开头统一为圈子动态消息**********************************************************
         */
        // 系统定制消息---好友新动态消息
        String ACTION_500 = "500";

        // 系统定制消息---好友新动态评论消息
        String ACTION_501 = "501";

        // 系统定制消息---好友新动态评论回复评论消息
        String ACTION_502 = "502";

        // 系统定制消息---删除动态
        String ACTION_503 = "503";

        // 系统定制消息---删除评论或取消点赞
        String ACTION_504 = "504";

        /*
         * ********************************************9开头统一为动作消息**********************************************************
         */
        // 系统定制消息---好友下线消息
        String ACTION_900 = "900";

        // 系统定制消息---好友上线消息
        String ACTION_901 = "901";

        // 系统定制消息---更新组织数据
        String ACTION_997 = "997";

        // 系统定制消息---更新用户数据
        String ACTION_998 = "998";

        // 系统定制消息---在另一台设备登录强制下线消息
        String ACTION_999 = "999";

    }

    interface MessageFormat {

        // 文字
        String FORMAT_TEXT = "0";

        // 图片
        String FORMAT_IMAGE = "1";

        // 语音
        String FORMAT_VOICE = "2";

        // 文件
        String FORMAT_FILE = "3";

        // 地图
        String FORMAT_MAP = "4";

        // 视频
        String FORMAT_VIDEO = "5";

        // 卡片
        String FORMAT_CARD = "6";

    }

    interface Bucket {
        String SQLITE = "sqlite";
        String CHAT_SPACE = "chat-space";
        String MOMENT_SPACE = "moment-space";
    }

    interface GroupMember {
        // 群主 1    群成员 0
        String RULE_FOUNDER = "1";
        String RULE_NORMAL = "0";

        // 是否禁言 0 否 1 是
        short MUTE_TYPE0 = 0;
        short MUTE_TYPE1 = 1;

        // 默认禁言时间为0，禁言存的是时间戳
        long MUTE_TIME_DEFAULT = 0L;
    }

    interface MessageSetting {
        // 是否开启自动回复功能 0 关闭  1开启
        short AUTO_TYPE0 = 0;
        short AUTO_TYPE1 = 1;
        // 回复时间类型 0秒 1分  2时
        short TIME_TYPE0 = 0;
        short TIME_TYPE1 = 1;
        short TIME_TYPE2 = 2;
    }

    interface MessageUserDto {
        /**
         * 聊天列表类型 0 用户 1 群组
         */
        short LIST_TYPE0 = 0;
        short LIST_TYPE1 = 1;
        /**
         * 是否显示状态 0  显示 1 隐藏 2置顶 3已退出该群
         */
        short SHOW_TYPE0 = 0;
        short SHOW_TYPE1 = 1;
        short SHOW_TYPE2 = 2;
        short SHOW_TYPE3 = 3;

        /**
         * 群成员
         */
        String ALL_GROUP_MEMBER_LIST = "allGroupMemberList";

        /**
         * 统计群人数
         */
        String ALL_GROUP_ITEM_IN_MESSAGE_USER_TABLE = "allGroupItemInMessageUserTable";

        /**
         * 群和群成员列表
         */
        String ALL_GROUP_WITH_GROUPMEMBER_LIST = "allGroupWithGroupMemberList";
        String ALL_GROUP_WITH_GROUPMEMBER_MAP = "allGroupWithGroupMemberMap";
    }

    interface Message {
        /**
         * 0 未接受 1 已接收 2 已读取 4 已删除
         */
        String STATE_NOT_RECEIVED = "0";
        String STATE_RECEIVED = "1";
        String STATE_READ = "2";
        String STATE_DELETE = "4";
        /**
         * 0 普通消息 1 群消息发送 3 群消息接收
         */
        String ACTION_0 = "0";
        String ACTION_1 = "1";
        String ACTION_3 = "3";
        /**
         * 双方都已删除
         */
        String HIDE_MESSAGE = "All";
    }
}
