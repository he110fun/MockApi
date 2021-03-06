package fun.hellofun.source;

import com.alibaba.fastjson.JSON;
import fun.hellofun.command.ItemType;
import fun.hellofun.command.topic.ImageTopic;
import fun.hellofun.command.topic.Topic;
import fun.hellofun.jUtils.predicate.empty.Empty;
import fun.hellofun.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * 该类由 <b>张东冬</b> 于 2020年3月17日 星期二 8时32分53秒 创建；<br>
 * 作用是：<b>图片源</b>；<br>
 */
class SourceImage extends AbstractSource<String> {

    /**
     * 为模板构建内置数据
     */
    static HashMap<String, Object> forFtl(int randomCount) {
        HashMap<String, Object> result = new HashMap<>(102);
        for (ImageTopic imageTopic : ImageTopic.values()) {
            result.put(imageTopic.getMark(), AbstractSource.image(imageTopic));
            result.put(imageTopic.getMark() + "s",
                    AbstractSource.take(ItemType.IMAGE,
                            new ArrayList<ImageTopic>() {{
                                add(imageTopic);
                            }},
                            randomCount
                    )
            );
            for (int i = Constants.INT_2; i <= Constants.INT_100; i++) {
                result.put(imageTopic.getMark() + "s" + i,
                        AbstractSource.take(ItemType.IMAGE,
                                new ArrayList<ImageTopic>() {{
                                    add(imageTopic);
                                }},
                                i
                        )
                );
            }
        }
        return result;
    }

    @Override
    protected String get(Topic topic) {
        return list(topic, 1).get(0);
    }

    @Override
    protected List<String> list(Topic topic, Integer count) {
        return take(count, pool(((ImageTopic) topic)));
    }

    private static List<String> ANIMALS = null;
    private static List<String> BOYS = null;
    private static List<String> CARS = null;
    private static List<String> FOODS = null;
    private static List<String> GIRLS = null;
    private static List<String> LANDSCAPES = null;
    private static List<String> PLANTS = null;
    private static List<String> BANNERS = null;

    /**
     * 从数据池中取值
     */
    private List<String> take(Integer count, List<String> fromPool) {
        List<String> result = new ArrayList<>();
        if (count == null) {
            count = DEFAULT_LIMIT;
        }
        for (Integer i = 0; i < count; i++) {
            result.add(fromPool.get(new Random().nextInt(fromPool.size())));
        }
        return result;
    }

    /**
     * 确定数据池
     */
    private List<String> pool(ImageTopic topic) {
        init();
        if (topic == null) {
            return all();
        }
        switch (topic) {
            case ANIMAL:
                return ANIMALS;
            case BOY:
                return BOYS;
            case CAR:
                return CARS;
            case BANNER:
                return BANNERS;
            case FOOD:
                return FOODS;
            case GIRL:
                return GIRLS;
            case PLANT:
                return PLANTS;
            case LANDSCAPE:
                return LANDSCAPES;
            default:
                return all();
        }
    }

    private List<String> all() {
        return new ArrayList<String>() {{
            addAll(ANIMALS);
            addAll(BOYS);
            addAll(CARS);
            addAll(FOODS);
            addAll(GIRLS);
            addAll(LANDSCAPES);
            addAll(PLANTS);
            addAll(BANNERS);
        }};
    }

    private void init() {
        if (Empty.yes(ANIMALS, BOYS, CARS, FOODS, GIRLS, LANDSCAPES, PLANTS, BANNERS)) {
            ANIMALS = JSON.parseArray(classpathFileString("image/animal.json"), String.class);
            BOYS = JSON.parseArray(classpathFileString("image/boy.json"), String.class);
            CARS = JSON.parseArray(classpathFileString("image/car.json"), String.class);
            FOODS = JSON.parseArray(classpathFileString("image/food.json"), String.class);
            GIRLS = JSON.parseArray(classpathFileString("image/girl.json"), String.class);
            LANDSCAPES = JSON.parseArray(classpathFileString("image/landscape.json"), String.class);
            PLANTS = JSON.parseArray(classpathFileString("image/plant.json"), String.class);
            BANNERS = JSON.parseArray(classpathFileString("image/banner.json"), String.class);
        }
    }
}
