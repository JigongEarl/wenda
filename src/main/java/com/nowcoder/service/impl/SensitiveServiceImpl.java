package com.nowcoder.service.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import com.nowcoder.model.Message;
import com.nowcoder.model.TriedNode;
/**
 * 敏感词处理
 * @author 86156
 *
 */
@Service
public class SensitiveServiceImpl implements InitializingBean{
	
	private static Logger logger = Logger.getLogger(SensitiveServiceImpl.class);
	
	private TriedNode rootNode = new TriedNode();
	
	private static final String DEFAULT_REPLACEMENT = "***";
	
	/**
	 * 新增敏感词
	 * @param word
	 */
	private void addWord(String word) {
		TriedNode tempNode = rootNode;
		//遍历每一个字节
		for(int i = 0; i < word.length(); i++) {
			Character c = word.charAt(i);
			//过滤空格
			if(isSymbol(c)) {
				continue;
			}
			TriedNode node = tempNode.getSubNode(c);
			//新增
			if(node == null) {
				node = new TriedNode();
				System.out.println("新增："+c);
				tempNode.addSubNode(c, node);
			}
			
			tempNode = node;
			//遍历结束，设置敏感词标识
			if(i == word.length() - 1) {
				tempNode.setKeywordEnd(true);
			}
		}
	}
	
	/**
	 * 判断是否为故意扰乱敏感词的字符（如￥、%、*、空格或者数字/英文字母等）
	 * @param c
	 * @return
	 */
	private boolean isSymbol(char c) {
        int ic = (int) c;
        // 0x2E80-0x9FFF 东亚文字范围   &&前是检查字符是否为ASCII 7位数字(即是否为ascii码)
        return !CharUtils.isAsciiAlphanumeric(c) && (ic < 0x2E80 || ic > 0x9FFF);
    }
	
	/**
	 * 该方法是InitializingBean类的待重写方法，它在spring容器启动时会默认被容器调用
	 * 我们借此在项目启动时加载敏感词库数据
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		/**
		 * 读取文件，新增敏感词
		 */
		InputStream is = null;
		BufferedReader br = null;
		try {
			is = Thread.currentThread().getContextClassLoader().getResourceAsStream("sensitiveWords.txt");
			br = new BufferedReader(new InputStreamReader(is));
			String lineTxt;
			while ((lineTxt = br.readLine()) != null) {
				addWord(lineTxt.trim());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("读取敏感词文件失败" + e.getMessage());
		} finally {
			br.close();
			is.close();
		}
	}
	

    /**
     * 过滤敏感词
     */
    public String filter(String text) {
        if (StringUtils.isBlank(text)) {
            return text;
        }
        String replacement = DEFAULT_REPLACEMENT;
        StringBuilder result = new StringBuilder();

        TriedNode tempNode = rootNode;
        int begin = 0; // 回滚数
        int position = 0; // 当前比较的位置
        
        while (position < text.length()) {
            char c = text.charAt(position);
            // 跳过干扰字符
            if (isSymbol(c)) {
                if (tempNode == rootNode) {
                    result.append(c);
                    ++begin;
                }
                ++position;
                continue;
            }

            tempNode = tempNode.getSubNode(c);

            // 当前位置的匹配结束
            if (tempNode == null) {
                // 以begin开始的字符串不存在敏感词
                result.append(text.charAt(begin));
                // 跳到下一个字符开始测试
                position = begin + 1;
                begin = position;
                // 回到树初始节点
                tempNode = rootNode;
            } else if (tempNode.isKeywordEnd()) {
                // 发现敏感词， 从begin到position的位置用replacement替换掉
                result.append(replacement);
            	position = position + 1;
                begin = position;
                tempNode = rootNode;
            } else {
                ++position;
            }
        }

        result.append(text.substring(begin));

        return result.toString();
    }
	

//	public static void main(String[] argv) {
//		SensitiveServiceImpl s = new SensitiveServiceImpl();
//		s.addWord("色情");
//		s.addWord("好色");
//		System.out.println(s.filter("AABBB你好￥^*%色哈哈， 你是色！！情"));
//		System.out.println(s.filter("色情内容"));
//		System.out.println(CharUtils.isAsciiAlphanumeric('a'));
//		System.out.println(CharUtils.isAsciiAlphanumeric('*'));
//		System.out.println(s.isSymbol('你')); //false
//		System.out.println(s.isSymbol('a')); //false
//		System.out.println(s.isSymbol(' ')); //true
//		System.out.println(s.isSymbol('*')); //true
//		System.out.println(s.isSymbol('&')); //true
//	}

}
