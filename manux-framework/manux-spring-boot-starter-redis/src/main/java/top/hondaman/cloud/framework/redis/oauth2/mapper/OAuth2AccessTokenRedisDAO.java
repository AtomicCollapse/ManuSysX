package top.hondaman.cloud.framework.redis.oauth2.mapper;

import cn.hutool.core.date.LocalDateTimeUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import top.hondaman.cloud.framework.common.util.collection.CollectionUtils;
import top.hondaman.cloud.framework.common.util.json.JsonUtils;
import top.hondaman.cloud.framework.redis.oauth2.dto.OAuth2AccessTokenDto;

import javax.annotation.Resource;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static top.hondaman.cloud.framework.redis.enums.RedisKeyConstants.OAUTH2_ACCESS_TOKEN;

@Repository
public class OAuth2AccessTokenRedisDAO {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 根据token从redis中查
     * @param accessToken
     * @return
     */
    public OAuth2AccessTokenDto get(String accessToken){
        String redisKey = formatKey(accessToken);
        return JsonUtils.parseObject(stringRedisTemplate.opsForValue().get(redisKey), OAuth2AccessTokenDto.class);
    }

    /**
     * 将token按照固定格式放入redis中
     * @param accessTokenDto
     */
    public void set(OAuth2AccessTokenDto accessTokenDto) {
        String redisKey = formatKey(accessTokenDto.getAccessToken());
        long time = LocalDateTimeUtil.between(LocalDateTime.now(), accessTokenDto.getExpiresTime(), ChronoUnit.SECONDS);
        if (time > 0) {
            stringRedisTemplate.opsForValue().set(redisKey, JsonUtils.toJsonString(accessTokenDto), time, TimeUnit.SECONDS);
        }
    }

    /**
     * 从redis中移除token
     * @param accessToken
     */
    public void delete(String accessToken) {
        String redisKey = formatKey(accessToken);
        stringRedisTemplate.delete(redisKey);
    }

    public void deleteList(Collection<String> accessTokens) {
        List<String> redisKeys = CollectionUtils.convertList(accessTokens, OAuth2AccessTokenRedisDAO::formatKey);
        stringRedisTemplate.delete(redisKeys);
    }

    /**
     * 按照格式将token组装成规范的redis key
     * @param accessToken
     * @return
     */
    private static String formatKey(String accessToken) {
        return String.format(OAUTH2_ACCESS_TOKEN, accessToken);
    }
}
