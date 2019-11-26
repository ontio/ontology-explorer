/*
 * Copyright (C) 2018 The ontology Authors
 * This file is part of The ontology library.
 *
 * The ontology is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The ontology is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with The ontology.  If not, see <http://www.gnu.org/licenses/>.
 */



package com.github.ontio.model.common;

/**
 * @author zhouq
 * @version 1.0
 * @date 2018/3/27
 */
public enum ClaimContextEnum {


    GITHUB_CLAIM("claim:github_authentication", "github authentication claim"),

    TWITTER_CLAIM("claim:twitter_authentication", "twitter authentication claim"),

    FACEBOOK_CLAIM("claim:facebook_authentication", "facebook authentication claim"),

    LINKEDIN_CLAIM("claim:linkedin_authentication", "linkedin authentication claim"),

    SFP_DL_CLAIM("claim:sfp_dl_authentication","shuftipro driver license authentication claim"),

    SFP_PP_CLAIM("claim:sfp_passport_authentication","shuftipro passport authentication claim"),

    SFP_ID_CLAIM("claim:sfp_idcard_authentication","shuftipro idcard authentication claim"),

    IDM_DL_CLAIM("claim:idm_dl_authentication","identitymind driver license authentication claim"),

    IDM_PP_CLAIM("claim:idm_passport_authentication","identitymind passport authentication claim"),

    IDM_ID_CLAIM("claim:idm_idcard_authentication","identitymind idcard authentication claim"),

    CFCA_CLAIM("claim:cfca_authentication", "cfca real-name authentication claim");

    private String desc;

    private String context;

    ClaimContextEnum(String context, String desc) {
        this.context = context;
        this.desc = desc;
    }

    public String context() {
        return this.context;
    }
    public String desc() {
        return this.desc;
    }

}
