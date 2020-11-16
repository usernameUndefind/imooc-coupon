package com.imooc.coupon.vo;

import com.imooc.coupon.constans.CouponStatus;
import com.imooc.coupon.constant.PeriodType;
import com.imooc.coupon.entity.Coupon;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.imooc.coupon.vo.TemplateRule.Expiration;
import org.apache.commons.lang.time.DateUtils;

/**
 * 用户优惠券的分类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponClassify {

    private List<Coupon> usable;

    private List<Coupon> used;

    private List<Coupon> expired;

    /**
     * 对当前的优惠券进行分类
     * @param coupons
     * @return
     */
    public static CouponClassify classify(List<Coupon> coupons) {
        List<Coupon> usable = new ArrayList<>(coupons.size());
        List<Coupon> used = new ArrayList<>(coupons.size());
        List<Coupon> expired = new ArrayList<>(coupons.size());

        coupons.forEach(c -> {
            boolean isTimeExpire;
            long curTime = new Date().getTime();
            Expiration expiration = c.getTemplateSDK().getRule().getExpiration();
            if (expiration.getPeriod().equals(PeriodType.REGULAR.getCode())) {
                isTimeExpire = expiration.getDeadline() <= curTime;
            } else {
                isTimeExpire = DateUtils.addDays(
                        c.getAssignTime(),
                        expiration.getGap()
                ).getTime() <= curTime;
            }

            if (c.getStatus() == CouponStatus.USED) {
                used.add(c);
            } else if (c.getStatus() == CouponStatus.EXPIRED || isTimeExpire) {
                expired.add(c);
            } else {
                usable.add(c);
            }
        });
        return new CouponClassify(usable, used, expired);
    }

}
