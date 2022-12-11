

const ErrorMessageInForm = ({errors}) => {

    let isThereErrors = errors.length > 0
    if(isThereErrors){
        return (
                    <ul className='text-danger'>
                    {
                        errors.map((error, key) => ( <li key={key}>{error}</li> ))
                    }
                    </ul>
        )
    }
}

export default ErrorMessageInForm