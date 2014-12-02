package com.sunlights.core.web;

import com.sunlights.BaseTest;
import com.sunlights.common.MsgCode;
import com.sunlights.common.vo.MessageVo;
import com.sunlights.common.vo.PageVo;
import com.sunlights.core.vo.BankCardFormVo;
import com.sunlights.core.vo.BankCardVo;
import models.CustomerSession;
import org.junit.Before;
import org.junit.Test;
import play.Logger;
import play.data.Form;
import play.db.jpa.JPA;
import play.libs.F;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.test.FakeRequest;
import web.TestUtil;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.*;

public class BankControllerTest extends BaseTest {

    private static Form<PageVo> pagerForm = Form.form(PageVo.class);
    private static Form<BankCardFormVo> bankCardForm = Form.form(BankCardFormVo.class);

    private static String BANK_CARD_NO = "6225885105575635";

    @Before
    public void init() {
        running(fakeApplication(inMemoryDatabase("test")), new Runnable() {
            public void run() {
                login("13811599308", "1");
            }
        });

    }

    @Test
    public void testCreateBankCard() throws Exception {
        running(fakeApplication(inMemoryDatabase("test")), new Runnable() {
            public void run() {
                String bankName = "招商银行";
                String bankSerial = "002";

                // create bank card
                Map<String, String> paramMap = new HashMap<String, String>();
                paramMap.put("bankName", bankName);
                paramMap.put("bankSerial", bankSerial);
                paramMap.put("bankCardNo", BANK_CARD_NO);

                Result result = getResult("/core/bank/bankcard/create", paramMap, cookie);

                Logger.info("result is " + contentAsString(result));
                assertThat(contentAsString(result)).contains(MsgCode.BANK_CARD_ADD_SUCCESS.getCode());


            }
        });

    }

    @Test
    public void testValidateBankCard() throws Exception {
        running(fakeApplication(inMemoryDatabase("test")), new Runnable() {
            public void run() {
                BankCardFormVo bankCardVo = new BankCardFormVo();
                bankCardVo.setBankCardNo(BANK_CARD_NO);
                bankCardVo.setBankCode("CCB");

                play.mvc.Result result = null;
                FakeRequest bankCardValidateRequest = fakeRequest(POST, "/core/bank/bankcard/validate");
                Map<String, String> paramMap = bankCardForm.bind(Json.toJson(bankCardVo)).data();

                FakeRequest formRequest = bankCardValidateRequest.withHeader("Content-Type", TestUtil.APPLICATION_X_WWW_FORM_URLENCODED).withFormUrlEncodedBody(paramMap);
                formRequest.withCookies(cookie);
                result = route(formRequest);

                Logger.info("result is " + contentAsString(result));
                assertThat(contentAsString(result)).contains(MsgCode.OPERATE_SUCCESS.getCode());
            }
        });
    }

    @Test
    public void testFindAndDeleteBankCards() throws Exception {
        running(fakeApplication(inMemoryDatabase("test")), new Runnable() {
            public void run() {

                FakeRequest findBankCards = fakeRequest(POST, "/core/bank/bankcards");
                play.mvc.Result result = null;
                // form request
                PageVo pageVo = new PageVo();
                Map<String, String> paramMap = pagerForm.bind(Json.toJson(pageVo)).data();
                Logger.info("[paramMap]" + paramMap);

                FakeRequest formRequest = findBankCards.withHeader("Content-Type", TestUtil.APPLICATION_X_WWW_FORM_URLENCODED).withFormUrlEncodedBody(paramMap);
                formRequest.withCookies(cookie);


                result = route(formRequest);

                Logger.info("result is " + contentAsString(result));

                assertThat(contentAsString(result)).contains(MsgCode.OPERATE_SUCCESS.getCode());

                MessageVo message = toMessageVo(result);
                Object value = message.getValue();
                if (value != null) {
                    List data = (ArrayList) value;
                    if (!data.isEmpty()) {
                        BankCardVo bankCardVo = Json.fromJson(Json.toJson(data.get(0)), BankCardVo.class);
                        // delete bank card
                        FakeRequest bankCardDeleteRequest = fakeRequest(POST, "/core/bank/bankcard/delete");
                        paramMap = bankCardForm.bind(Json.toJson(bankCardVo)).data();

                        FakeRequest formBankCardDeleteRequest = bankCardDeleteRequest.withHeader("Content-Type", TestUtil.APPLICATION_X_WWW_FORM_URLENCODED).withFormUrlEncodedBody(paramMap);
                        formBankCardDeleteRequest.withCookies(cookie);
                        result = route(formBankCardDeleteRequest);

                        Logger.info("result is " + contentAsString(result));
                        assertThat(contentAsString(result)).contains(MsgCode.BANK_CARD_DELETE_SUCCESS.getCode());
                    }
                }

            }
        });
    }

    @Test
    public void testFindBankByBankCardNo() throws Exception {
        running(fakeApplication(inMemoryDatabase("test")), new Runnable() {
            public void run() {
                BankCardVo bankCardVo = new BankCardVo();
                bankCardVo.setBankCardNo("6225885105574736");
                FakeRequest findBankRequest = fakeRequest(POST, "/core/bank/findbybankcard");
                play.mvc.Result result = null;
                // form request
                Map<String, String> paramMap = bankCardForm.bind(Json.toJson(bankCardVo)).data();
                Logger.info("[paramMap]" + paramMap);
                FakeRequest formRequest = findBankRequest.withHeader("Content-Type", TestUtil.APPLICATION_X_WWW_FORM_URLENCODED).withFormUrlEncodedBody(paramMap);
                result = route(formRequest);
                Logger.info("result is " + contentAsString(result));
                assertThat(contentAsString(result)).contains(MsgCode.OPERATE_SUCCESS.getCode());
            }
        });
    }

    @Test
    public void testFindBanks() throws Exception {
        running(fakeApplication(inMemoryDatabase("test")), new Runnable() {
            public void run() {
                PageVo pageVo = new PageVo();
                FakeRequest banksRequest = fakeRequest(POST, "/core/banks");
                play.mvc.Result result = null;
                // form request
                Map<String, String> paramMap = pagerForm.bind(Json.toJson(pageVo)).data();
                Logger.info("[paramMap]" + paramMap);
                FakeRequest formRequest = banksRequest.withHeader("Content-Type", TestUtil.APPLICATION_X_WWW_FORM_URLENCODED).withFormUrlEncodedBody(paramMap);
                result = route(formRequest);
                Logger.info("result is " + contentAsString(result));
                assertThat(contentAsString(result)).contains("0000");
            }
        });
    }
}
