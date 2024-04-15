# khuropbox
```mermaid
digraph {
    rankdir=TD;

    VPC [label="VPC"];
    S3 [label="S3"];
    RDS [label="RDS"];
    ASG [label="ASG"];
    ELB [label="ELB"];
    Cognito [label="Cognito"];
    Route53 [label="Route 53"];
    FC7_A [label="FC7 인스턴스 A (프론트엔드 서버)"];
    FC2_R [label="FC2 인스턴스 R (프론트엔드 서버)"];
    FC7_C [label="FC7 인스턴스 C (백엔드 서버)"];
    FC2_D [label="FC2 인스턴스 D (백엔드 서버)"];

    VPC -> S3;
    VPC -> RDS;
    VPC -> ASG;
    VPC -> ELB;
    VPC -> Cognito;

    FC7_A -> ELB;
    FC2_R -> ELB;
    ELB -> Route53;
    Route53 -> "api.fsoftwaregineer.com";
    "api.fsoftwaregineer.com" -> RDS;

    FC7_C -> ELB;
    FC2_D -> ELB;
    ELB -> Route53;
    Route53 -> "app.Isoftwaregineer.com";
    "app.Isoftwaregineer.com" -> RDS;

    Route53 -> "M.50.RDS";

    node [style=filled];
    FC7_A [fillcolor="#f7f8fa"];
    FC7_C [fillcolor="#f7f8fa"];
    FC2_R [fillcolor="#f7f8fa"];
    FC2_D [fillcolor="#f7f8fa"];
    RDS [fillcolor="#f0fff0"];
    S3 [fillcolor="#e6f0ff"];
    ASG [fillcolor="#d6e9c6"];
    ELB [fillcolor="#fff0f5"];
    Cognito [fillcolor="#c4d3f0"];
    Route53 [fillcolor="#c0c0c0"];
}
```
