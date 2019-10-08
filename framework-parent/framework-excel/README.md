# framework-excel

## 1.导出
### 1.1 注解
1. @ExportSheet

这个注解作用在需要导出的实体类上，实体类必须满足Java Bean规范(获取字段的值，会调用该字段对应的get方法)

|   属性  |  类型  |  默认值  |  功能  |
| :----: | :----: | :----:  | :----  |
| name  | String | "" | Sheet的名称 |
| title  | String | "" | Sheet的标题 |
| titleHeight  | short | 36 | Sheet标题行的行高 |
| columnTitleHeight  | short | 20 | Sheet列标题行的行高 |
| indexName  | String | 序号 | Sheet序号列的标题名称 |
| indexWidth  | int | 8 | 序号列的宽度 |
| number  | int | 1 | Excel中生成Sheet的数量 |
| fontName  | String | 宋体 | Sheet中使用的字体名称 |
| addIndex  | boolean | true | 是否添加序号列 |
| mergeIndex  | boolean | true | 行数据中有Collection时是否合并序号列 |
| freezeTitle  | boolean | false | 是否冻结标题行 |
| type  | ExcelType | XLSX | 生成的Excel类型, 可指定XLS, XLSX |
| cellStyleBuilder  | Class<? extends CellStyleBuilder> | DefaultCellStyleBuilder.class | 单元样式构建类, 用于构建标题和内容样式, 以及设置合并单元格边框 |
| indexBuilder  | Class<? extends IndexBuilder> | DefaultIndexBuilder.class | 序号列数据构建类, 用于构建序号列的数据 |

2. @ExportTitle

这个注解作用在需要导出的字段和方法上, 用于指定需要生成的列; 同样可以作用于父类的字段和方法上

|   属性  |  类型  |  默认值  |  功能  |
| :----: | :----: | :----:  | :----  |
| title  | String | -- | 列标题名称 |
| order  | int | -- | 列的排序 |
| width  | int | 12 | 列的宽度 |
| height  | int | 20 | 内容的行高 |
| prefix  | String | "" | 值拼接前缀 |
| suffix  | String | "" | 值拼接后缀 |
| replaces  | String[] | {} | 值的替换, 使用两个下划线(<b>__</b>)进行分割 |
| groupName  | String | "" | 分组名称, 相同组名会生成二级标题 |
| convertClass  | Class<?> | Void.class | 值的转换类 |
| convertMethod  | String | "" | 值的转换类中的方法名称(**优先级最高**) |
| enumMethod  | String | "" | 字段或方法返回值为枚举类型可以指定调用枚举的方法(优先级低于convertMethod) |
| isEntity  | boolean | false | 字段是否是一个实体```@ExcelEntity```, **仅可作用于字段, 在关联实体中不生效** |
| isCollection  | boolean | false | 字段是否是一个集合```@Collection```, **仅可作用于字段, 在关联实体中不生效** |
| merge  | boolean | true | 行数据中有Collection时是否合并非集合列的行 |
| fillSame  | boolean | true | 不合并非集合列行时(```merge = false```), 是否填充相同数据 |
| fillValue  | String | "" | 不合并非集合列行时(```merge = false```), 不填充相同数据(```fillSame = false```), 可以指定填充数据 |

3. @ExcelEntity

这个注解作用在导出实体类中关联的其它实体类上, 配合```@ExportTitle(isEntity = true)```
使用

### 1.2 导出使用

#### 1.2.1 对象定义
```
public class ConvertUtil {

	public String convertAge(Integer age) {
		return age + "岁";
	}
	
	
	public String convertProvince(String province) {
		return province + "省";
	}
	
	public String convertHobby(String hobby) {
		if ("1".equals(hobby)) {
			return "JAVA";
		} else if ("2".equals(hobby)) {
			return "PHP";
		}
		return null;
	}
}

@Getter
public class UserBase {
	
	@ExportTitle(title = "学号", order = 1, merge = true)
	private String sn = "10086";
	
	@ExportTitle(title = "性别", order = 5, replaces = {"0__女", "1__男"}, suffix = "性")
	public Integer getGender() {
		return 1;
	}

}

@Data
@EqualsAndHashCode(callSuper = true)
@ExportSheet(title = "用户名单", name = "用户", number = 1, mergeIndex = true, fontName = "微软雅黑", freezeTitle = true)
public class User extends UserBase {
	
	@ExportTitle(title = "姓名", order = 2)
	private String name = "李四";
	
	@ExportTitle(title = "年龄", order = 4, convertClass = ConvertUtil.class, convertMethod = "convertAge")
	private Integer age;
	
	@ExportTitle(title = "父亲姓名", order = 5)
	private String fatherName = "父亲";
	
	@ExportTitle(title = "母亲姓名", order = 3)
	private String motherName = "母亲";
	
	@ExportTitle(title = "地址", order = 6, isEntity = true)
	private Address address = new Address().setCity("杭州").setProvince("浙江");
	
	@ExportTitle(title = "籍贯", order = 11, isCollection = true)
	private List<Address> nativePlaces = Arrays.asList(
			new Address().setCity("成都").setProvince("四川"),
			new Address().setCity("璧山").setProvince("重庆")
		);
		
	@ExportTitle(title = "用户类型", order = 9)
	private UserType userType = UserType.STUDENT;
	
	@ExportTitle(title = "父亲年龄", order = 4, convertClass = ConvertUtil.class, convertMethod = "convertAge")
	public Integer fatherAge() {
		return 40;
	}

}
```

#### 1.2.2 导出工具使用

```
public class ExportTest {
	
	@Test
	public void testExport() throws Exception {
		User user = new User();
		user.setAge(10);
		List<User> userList = Arrays.asList(user, new User());
		
		FileOutputStream os = new FileOutputStream("E:/user.xlsx");
		ExcelExportUtils.export(User.class, userList, os);
	}
}
```

