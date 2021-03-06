package fun.hellofun.command.topic;

import fun.hellofun.command.ItemType;
import fun.hellofun.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 该类由 <b>张东冬</b> 于 2020年3月17日 星期二 8时39分33秒 创建；<br>
 * 作用是：<b>抽象主题</b>；<br>
 *
 * @author zdd
 */
public interface Topic {

    /**
     * 获取唯一标识
     *
     * @return 主题的唯一标识
     */
    String getMark();


    /**
     * 该方法由 <b>张东冬</b> 于 2020年3月18日 星期三 19时35分27秒 创建；<br>
     * 作用是：<b>所有合法主题</b>；<br>
     *
     * @return 返回所有的合法主题
     */
    static List<String> allMark() {
        List<String> result = new ArrayList<>();
        for (ImageTopic imageTopic : ImageTopic.values()) {
            result.add(imageTopic.getMark());
        }
        for (TextTopic textTopic : TextTopic.values()) {
            result.add(textTopic.getMark());
        }
        for (VideoTopic videoTopic : VideoTopic.values()) {
            result.add(videoTopic.getMark());
        }
        return result;
    }

    /**
     * 获取主题，可能是多个
     *
     * @param parts 用户输入命令的各个部分
     * @param type  元素类型
     * @return 用户输入的有效主题
     */
    static List<Topic> extract(String[] parts, ItemType type) {
        List<Topic> result = new ArrayList<>();
        if (type == null) {
            return result;
        }

        Map<String, Topic> map = new HashMap<>(50);

        Topic[] topics = null;
        switch (type) {
            case IMAGE:
                topics = ImageTopic.values();
                break;
            case TEXT:
                topics = TextTopic.values();
                break;
            case VIDEO:
                topics = VideoTopic.values();
                break;
            default:
                return result;
        }
        for (Topic topic : topics) {
            map.put(topic.getMark(), topic);
        }

        for (String part : parts) {
            // 可能是逗号拼接的多个主题
            String temp = part;
            if (part.contains("topic=")) {
                temp = part.split("=")[1];
            }
            // 遍历形如   animal,boy,girl
            for (String s : temp.split(Constants.COMMA)) {
                Topic topic = map.get(s);
                if (topic != null && !result.contains(topic)) {
                    result.add(topic);
                }
            }
        }
        return result;
    }
}
