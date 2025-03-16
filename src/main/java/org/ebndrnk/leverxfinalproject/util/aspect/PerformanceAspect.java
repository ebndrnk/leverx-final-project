package org.ebndrnk.leverxfinalproject.util.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.ebndrnk.leverxfinalproject.util.feature.service.FeatureFlagService;
import org.ebndrnk.leverxfinalproject.util.feature.FeatureFlags;
import org.springframework.stereotype.Component;

/**
 * PerformanceAspect
 * <p>
 * This aspect measures the execution time of methods in the repository package.
 * It logs the duration of each method execution to help identify performance issues.
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class PerformanceAspect {

    private final FeatureFlagService featureFlagService;
    private long startTime;

    /**
     * Advice that runs before any method in the repository package.
     * It records the current system time before the method execution starts.
     *
     * @param joinPoint the join point representing the method call
     */
    @Before("execution(* org.ebndrnk.leverxfinalproject.repository..*(..))")
    public void beforeMethod(JoinPoint joinPoint) {
        startTime = System.currentTimeMillis();
    }

    /**
     * Advice that runs after any method in the repository package.
     * It calculates the duration of the method execution and logs it.
     * (if feature-flag equal false, the method doesn't display a message)
     * @param joinPoint the join point representing the method call
     */
    @After("execution(* org.ebndrnk.leverxfinalproject.repository..*(..))")
    public void afterMethod(JoinPoint joinPoint) {
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        if(featureFlagService.isFeatureEnabled(FeatureFlags.CALL_PERFORMANCE_ASPECT_FEATURE_FLAG.getValue())){
            log.info("Method {}: execution time: {} ms", joinPoint.getSignature().getName(), duration);
        }
    }
}
