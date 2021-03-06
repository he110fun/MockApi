package fun.hellofun.source;

import fun.hellofun.command.ItemType;
import fun.hellofun.command.Limit;
import fun.hellofun.command.TimeFormat;
import fun.hellofun.command.topic.ImageTopic;
import fun.hellofun.command.topic.TextTopic;
import fun.hellofun.command.topic.Topic;
import fun.hellofun.command.topic.VideoTopic;
import fun.hellofun.jUtils.predicate.empty.Empty;
import okio.Okio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 该类由 <b>张东冬</b> 于 2020年3月16日 星期一 19时06分00秒 创建；<br>
 * 作用是：<b>内嵌数据源</b>；<br>
 *
 * @author zdd
 */
public abstract class AbstractSource<T> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractSource.class);

    /**
     * 默认list接口的元素个数
     */
    public static final int DEFAULT_LIMIT = 20;

    /**
     * 文件默认父路径
     */
    public static final String DEFAULT_FILE_PATH = "";

    private static final SourceText TEXT = new SourceText();

    private static final SourceImage IMAGE = new SourceImage();

    private static final SourceVideo VIDEO = new SourceVideo();

    /**
     * 获取单个元素
     *
     * @param topic 元素所属主题
     * @return 单个元素
     */
    protected abstract T get(Topic topic);

    /**
     * 获取多个元素
     *
     * @param topic 元素所属主题
     * @param count 元素个数
     * @return 一个集合
     */
    protected abstract List<T> list(@Nullable Topic topic, Integer count);

    /**
     * 构建内置数据
     */
    public static Map<String, Object> forFtl(ItemType type, int randomCount) {
        switch (type) {
            case TEXT:
                return SourceText.forFtl(randomCount);
            case IMAGE:
                return SourceImage.forFtl(randomCount);
            case VIDEO:
                return SourceVideo.forFtl(randomCount);
            default:
                return null;
        }
    }

    /**
     * 单个文本
     */
    public static String text(TextTopic textTopic) {
        return TEXT.get(textTopic);
    }

    /**
     * 单张图片
     */
    public static String image(ImageTopic imageTopic) {
        return IMAGE.get(imageTopic);
    }

    /**
     * 单个视频
     */
    public static String video(VideoTopic videoTopic) {
        return VIDEO.get(videoTopic);
    }

    /**
     * 取多个 文本、图片、视频
     */
    public static <T extends Topic> List<String> take(ItemType type, List<T> topics, Integer count) {
        AbstractSource source = null;
        if (type == ItemType.IMAGE) {
            source = IMAGE;
        } else if (type == ItemType.TEXT) {
            source = TEXT;
        } else if (type == ItemType.VIDEO) {
            source = VIDEO;
        } else {
            throw new UnsupportedOperationException("当前ItemType不被支持，ItemType=" + type);
        }
        if (Empty.yes(topics)) {
            return source.list(null, count);
        } else if (topics.size() == 1) {
            return source.list(topics.get(0), count);
        } else {
            List<String> result = new ArrayList<>();
            do {
                result.addAll(source.list(topics.get(new Random().nextInt(topics.size())), new Random().nextInt(3)));
            } while (count > result.size());
            return result.subList(0, count);
        }
    }

    /**
     * 单个整数
     */
    public static int integer(Limit limit) {
        if (limit == null) {
            limit = Limit.DEFAULT_LIMIT;
        }
        return limit.getLowerLimit().intValue()
                + new Random().nextInt(limit.getUpperLimit().intValue() - limit.getLowerLimit().intValue());
    }

    /**
     * 多个整数
     */
    public static List<Integer> integers(Limit limit, Integer count) {
        ArrayList<Integer> result = new ArrayList<>();
        for (Integer i = 0; i < count; i++) {
            result.add(integer(limit));
        }
        return result;
    }

    /**
     * 单个浮点数
     */
    public static double floatt(Limit limit) {
        if (limit == null) {
            limit = Limit.DEFAULT_LIMIT;
        }
        double v = new Random().nextDouble();
        return (BigDecimal.valueOf(v).doubleValue() - BigDecimal.valueOf(v).intValue())
                + limit.getLowerLimit().intValue()
                + new Random().nextInt(limit.getUpperLimit().intValue() - limit.getLowerLimit().intValue());
    }

    /**
     * 多个浮点数
     */
    public static List<Double> floats(Limit limit, Integer count) {
        ArrayList<Double> result = new ArrayList<>();
        for (Integer i = 0; i < count; i++) {
            result.add(floatt(limit));
        }
        return result;
    }

    /**
     * 单个布尔值
     */
    public static boolean bool() {
        return new Random().nextInt() % 2 == 0;
    }

    /**
     * 多个布尔值
     */
    public static List<Boolean> bools(Integer count) {
        ArrayList<Boolean> result = new ArrayList<>();
        for (Integer i = 0; i < count; i++) {
            result.add(bool());
        }
        return result;
    }

    /**
     * 单个时间
     */
    public static Object time(TimeFormat format) {
        long l = System.currentTimeMillis() - new Random().nextInt(1_000_000_000) * 100L;
        if (format == null) {
            return l;
        }
        return new SimpleDateFormat(format.getValue()).format(l);
    }

    /**
     * 多个时间
     */
    public static Object times(TimeFormat format, Integer count) {
        ArrayList result = new ArrayList();
        for (Integer i = 0; i < count; i++) {
            result.add(time(format));
        }
        return result;
    }


    /**
     * 类路径文件中的内容（字符串形式）
     */
    protected String classpathFileString(String path) {
        try {
            return Okio.buffer(Okio.source(new ClassPathResource(path).getInputStream())).readUtf8();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return "";
    }

}
