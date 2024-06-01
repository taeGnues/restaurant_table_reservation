package com.zerobase.tablereservation.common.constant;

public enum Authority {
    ROLE_MANAGER{
        @Override
        public String toString() {
            return "MANAGER";
        }
    },
    ROLE_CUSTOMER{
        @Override
        public String toString() {
            return "CUSTOMER";
        }
    }
}
