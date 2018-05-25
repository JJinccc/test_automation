package com.ffan.qa.network;

import javax.ws.rs.core.Response;

import com.ffan.qa.common.Logger;
import com.ffan.qa.utils.JsonUtil;
import com.ffan.qa.utils.StringUtil;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

public class APIResponse {

	private Response response;
	private String body;
	private Long requestTime;
	
	public APIResponse(Response response, Long requestTime)
	{
		this.response = response;
		this.requestTime = requestTime;
	}

	/**
	 * 验证请求返回状态码
	 * @param status
	 * @return
	 */
	public APIResponse assertStatus(int status)
	{
		Assert.assertEquals(status, response.getStatus());
		return this;
	}

	/**
	 * 验证返回内容是否与期望相符
	 * @param content
	 * @return
	 */
	public APIResponse assertBody(String content)
	{
		String body = getBody();
		Assert.assertEquals(content, body);
		return this;
	}

	/**
	 * 验证返回Body中是否包含期望内容
	 * @param content
	 * @return
	 */
	public APIResponse assertBodyContains(String content)
	{
		String body = getBody();
		Assert.assertEquals(true, body.contains(content));
		return this;
	}

	/**
	 * 验证返回Json对象的值是否与预期相符
	 * @param keyExpression
	 * @param expectedValue
	 * @return
	 */
	public APIResponse assertJValue(String keyExpression, String expectedValue)
	{
		return assertJValue(keyExpression, expectedValue, "");
	}

	/**
	 * 验证返回Json对象的值是否大于预期
	 * @param keyExpression
	 * @param baseValue
	 * @return
	 */
	public APIResponse assertJValueBigger(String keyExpression, int baseValue) {
		return assertJValueBigger(keyExpression, baseValue, "");
	}

	/**
	 * 验证返回Json对象的值是否大于预期
	 * @param keyExpression
	 * @param baseValue
	 * @param message
	 * @return
	 */
	public APIResponse assertJValueBigger(String keyExpression, int baseValue, String message) {
		Object object = getJValue(keyExpression);
		Assert.assertNotNull(object, "对象" + keyExpression + "为NULL.");
		double realVal = Double.parseDouble(object.toString());
		Assert.assertTrue(realVal > baseValue, message);
		return this;
	}

	/**
	 * 验证返回Json对象的值是否与预期相符
	 * @param keyExpression
	 * @param expectedValue
	 * @param message
	 * @return
	 */
	public APIResponse assertJValue(String keyExpression, String expectedValue, String message)
	{
		Object object = getJValue(keyExpression);
		Assert.assertEquals(object.toString(), expectedValue, message);
		return this;
	}

	/**
	 * 验证Json对象中的数组对象长度是否与预期相符
	 * @param keyExpression
	 * @param expectedSize
	 * @return
	 */
	public APIResponse assertJSize(String keyExpression, int expectedSize) {
		return assertJSize(keyExpression, expectedSize, "");
	}

	/**
	 * 验证Json对象中的数组对象长度是否与预期相符
	 * @param keyExpression
	 * @param expectedSize
	 * @param message
	 * @return
	 */
	public APIResponse assertJSize(String keyExpression, int expectedSize, String message) {
		ArrayList<Object> obj = (ArrayList<Object>)getJValue(keyExpression);
		Assert.assertEquals(obj.size(), expectedSize, message);
		return this;
	}

	/**
	 * 验证Json对象中的数组对象长度是否大于预期值
	 * @param keyExpression
	 * @param baseSize
	 * @return
	 */
	public APIResponse assertJSizeBigger(String keyExpression, int baseSize) {
		return assertJSizeBigger(keyExpression, baseSize, "");
	}

	/**
	 * 验证Json对象中的数组对象长度是否大于预期值
	 * @param keyExpression
	 * @param baseSize
	 * @param message
	 * @return
	 */
	public APIResponse assertJSizeBigger(String keyExpression, int baseSize, String message) {
		ArrayList<Object> obj = (ArrayList<Object>)getJValue(keyExpression);
		Assert.assertTrue(obj.size() > baseSize, message);
		return this;
	}

	/**
	 * 验证Json对象中的数组对象长度是否小于预期值
	 * @param keyExpression
	 * @param baseSize
	 * @return
	 */
	public APIResponse assertJSizeLess(String keyExpression, int baseSize) {
		return assertJSizeLess(keyExpression, baseSize, "");
	}

	/**
	 * 验证Json对象中的数组对象长度是否小于预期值
	 * @param keyExpression
	 * @param baseSize
	 * @param message
	 * @return
	 */
	public APIResponse assertJSizeLess(String keyExpression, int baseSize, String message) {
		ArrayList<Object> obj = (ArrayList<Object>)getJValue(keyExpression);
		Assert.assertTrue(obj.size() < baseSize, message);
		return this;
	}

	/**
	 * 验证Json对象是否为空
	 * @param keyExpression
	 * @return
	 */
	public APIResponse assertJNotEmpty(String keyExpression)
	{
		return assertJNotEmpty(keyExpression, "");
	}

	/**
	 * 验证Json对象是否为空
	 * @param keyExpression
	 * @param message
	 * @return
	 */
	public APIResponse assertJNotEmpty(String keyExpression, String message)
	{
		Object object = getJValue(keyExpression);
		Assert.assertTrue(null != object && !StringUtil.isNullOrEmpty(object.toString()), message);
		return this;
	}

	/**
	 * 验证Json数组对象不为空
	 * @param keyExpression
	 * @param message
	 * @return
	 */
	public APIResponse assertJListNotEmpty(String keyExpression, String message)
	{
		Object object = getJValue(keyExpression);
		Assert.assertTrue(null != object, message);

		assertJSizeBigger(keyExpression, 0, message);
		return this;
	}

	/**
	 * 验证指定数据返回为Null
	 * @param keyExpression
	 * @param message
	 * @return
	 */
	public APIResponse assertJNull(String keyExpression, String message) {
		Object object = getJValue(keyExpression);
		Assert.assertNull(object, message);
		return this;
	}

	public APIResponse assertJListNullOrEmpty(String keyExpression, String message) {
		Object object = getJValue(keyExpression);
		if (null != object) {
			List<Object> arr = (ArrayList<Object>)object;
			if (null != arr && arr.size() > 0) {
				Assert.fail(message);
			}
		}
		return this;
	}

	/**
	 * 返回请求响应内容
	 * @return
	 */
	public String getBody()
	{
		if (body == null){
			body = response.readEntity(String.class);
		}
		return body;
	}

	/**
	 * 返回Json对象
	 * @param keyExpression
	 * @return
	 */
	public Object getJValue(String keyExpression) {
		String body = getBody();
		Object object = JsonUtil.getValue(body, keyExpression);

		//Assert.assertNotNull(object, String.format("未发现对象%s, Response body: %s", keyExpression, getBody()));

		return object;
	}

	/**
	 * 将对象转换类型返回
	 * @param keyExpression
	 * @param <T>
	 * @return
	 */
	public <T> T getValue(String keyExpression) {
		Object obj = getJValue(keyExpression);
		return (T)obj;
	}

	/**
	 * 将返回内容转化为实体类进行返回
	 * @param t
	 * @param <T>
	 * @return
	 */
	public <T> T getBody(Class<T> t)
	{
		return response.readEntity(t);
	}

	public APIResponse printResponse() {
		Logger.log("返回Body：" + getBody());
		return this;
	}

	public Long getRequestTime() {
		return requestTime;
	}
}
