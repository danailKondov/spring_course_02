package ru.otus.spring02.security;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;


@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        return new CustomMethodSecurityExpressionHandler();
    }

    private class CustomMethodSecurityExpressionHandler
            extends DefaultMethodSecurityExpressionHandler {
        private AuthenticationTrustResolver trustResolver =
                new AuthenticationTrustResolverImpl();

        @Override
        protected MethodSecurityExpressionOperations createSecurityExpressionRoot(
                Authentication authentication, MethodInvocation invocation) {
            CustomMethodSecurityExpressionRoot root =
                    new CustomMethodSecurityExpressionRoot(authentication);
            root.setPermissionEvaluator(getPermissionEvaluator());
            root.setTrustResolver(this.trustResolver);
            root.setRoleHierarchy(getRoleHierarchy());
            return root;
        }
    }

    private class CustomMethodSecurityExpressionRoot
            extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

        private Object filterObject;
        private Object returnObject;

        public CustomMethodSecurityExpressionRoot(Authentication authentication) {
            super(authentication);
        }

        public boolean hasNameOf(String permissionHolder) {
            String userName = ((UserDetails) this.getPrincipal()).getUsername();
            return userName.equals(permissionHolder);
        }

        @Override
        public Object getFilterObject() {
            return this.filterObject;
        }

        @Override
        public Object getReturnObject() {
            return this.returnObject;
        }

        @Override
        public Object getThis() {
            return this;
        }

        @Override
        public void setFilterObject(Object obj) {
            this.filterObject = obj;
        }

        @Override
        public void setReturnObject(Object obj) {
            this.returnObject = obj;
        }
    }
}
