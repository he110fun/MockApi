package fun.hellofun.command.topic;

/**
 * 该类由 <b>张东冬</b> 于 2020年3月18日 星期三 18时58分42秒 创建；<br>
 * 作用是：<b>文本主题</b>；<br>
 *
 * @author zdd
 */
public enum TextTopic implements Topic {
    /**
     * 姓名
     */
    NAME("name"),
    /**
     * 心灵鸡汤
     */
    SOUP("soup");

    private String mark;

    TextTopic(String mark) {
        this.mark = mark;
    }

    @Override
    public String getMark() {
        return mark;
    }

}
