%extend NdbRecAttr {
public:
    void value_any(const NdbRecAttr** OutValueProxy) {
        *OutValueProxy=self;
    }
};
