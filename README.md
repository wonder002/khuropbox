# khuropbox

graph TD
    subgraph 클라우드
        S3[S3]
        RDS[RDS]
        ASG[ASG]
        ELB[ELB]
        Cognito[Cognito]
    end

    VPC[VPC] --> S3
    VPC --> RDS
    VPC --> ASG
    VPC --> ELB
    VPC --> Cognito
    
    FC7_A[FC7 인스턴스 A (프론트엔드 서버)] --> ELB
    FC2_R[FC2 인스턴스 R (프론트엔드 서버)] --> ELB

    ELB --> api.fsoftwaregineer.com
    api.fsoftwaregineer.com --> RDS

    FC7_C[FC7 인스턴스 C (백엔드 서버)] --> ELB
    FC2_D[FC2 인스턴스 D (백엔드 서버)] --> ELB

    ELB --> app.Isoftwaregineer.com
    app.Isoftwaregineer.com --> RDS

    style FC7_A, FC7_C, FC2_R, FC2_D default #f7f8fa;
    style RDS #f0fff0;
    style S3 #e6f0ff;
    style ASG #d6e9c6;
    style ELB #fff0f5;
    style Cognito #c4d3f0;
