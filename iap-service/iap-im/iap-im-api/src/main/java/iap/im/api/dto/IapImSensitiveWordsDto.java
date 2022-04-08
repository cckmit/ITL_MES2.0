/*
 * Copyright ? 2017 海通安恒科技有限公司.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ***************************************************************************************
 *                                                                                     *
 *                        Website : http://www.htah.com.cn/                            *
 *                                                                                     *
 ***************************************************************************************
 */
package iap.im.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO类-敏感词汇表
 *
 * @author SifrHiME
 * @date 2020-10-14
 * @since jdk1.8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class IapImSensitiveWordsDto implements Serializable {

    /**
     * 主键ID
     */
    private String id;

    /**
     * 词汇
     */
    private String word;

    /**
     * 词汇类型(1.反动 2.暴恐 3.民生 4.色情 5.贪腐 6.其他)
     */
    private Integer type;

    /**
     * 创建人
     */
    private String creater;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 最后修改人
     */
    private String lastUpdateBy;

    /**
     * 最后修改时间
     */
    private Date lastUpdateDate;
}
