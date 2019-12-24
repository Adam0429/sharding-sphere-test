# sharing-sphere-test
testing project for sharding-sphere


需求说明：

将职工按照不同的年龄段进行分库，按照不同的性别进行分表。具体需求为:age在[0, 50)的，分到younger库;age在[50, 100)的，分到older库;age>=100的，分到other库。

其中younger库和older库又按照性别进行分表，男的录入staff_man表，女的录入staff_woman表，other库不区分男女，不论男女都直接录入staff表。
